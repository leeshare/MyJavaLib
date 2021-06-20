package org.lixl.mr.clickstream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.lixl.mr.mrbean.PageViewsBean;
import org.lixl.mr.mrbean.VisitBean;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 输入数据：pageviews模型结果数据
 * 从pageviews模型结果数据中进一步梳理出visit模型
 * sessionid    start-time  out-time    start-page  out-page    pagecounts  ...
 *
 */
public class ClickStreamVisit {

    static class ClickStreamVisitMapper extends Mapper<LongWritable, Text, Text, PageViewsBean> {

        PageViewsBean pvBean = new PageViewsBean();
        Text k = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //super.map(key, value, context);
            String line = value.toString();
            String[] fields = line.split("\001");
            int step = 0;
            try {
                step = Integer.parseInt(fields[5]);
            }catch (Exception e){}
            pvBean.set(fields[0], fields[1], fields[2], fields[3], fields[4], step, fields[6], fields[7], fields[8], fields[9]);
            k.set(pvBean.getSession());
            context.write(k, pvBean);
        }
    }

    static class ClickStreamVisitReducer extends Reducer<Text, PageViewsBean, NullWritable, VisitBean> {

        @Override
        protected void reduce(Text key, Iterable<PageViewsBean> values, Context context) throws IOException, InterruptedException {
            // 将 pvBeans 按照 step 排序
            ArrayList<PageViewsBean> pvBeansList = new ArrayList<>();
            for(PageViewsBean pvBean : values){
                PageViewsBean bean = new PageViewsBean();
                try {
                    BeanUtils.copyProperties(bean, pvBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Collections.sort(pvBeansList, new Comparator<PageViewsBean>() {
                @Override
                public int compare(PageViewsBean o1, PageViewsBean o2) {
                    return o1.getStep() > o2.getStep() ? 1 : -1;
                }
            });

            // 取这次visit的首尾 pageView记录，将数据放入 VisitBean中。
            VisitBean visitBean = new VisitBean();
            // 取visit的首记录
            visitBean.setInPage(pvBeansList.get(0).getRequest());
            visitBean.setInTime(pvBeansList.get(0).getTimestr());
            // 取visit的尾记录
            visitBean.setOutPage(pvBeansList.get(pvBeansList.size() - 1).getRequest());
            visitBean.setOutTime(pvBeansList.get(pvBeansList.size() - 1).getTimestr());
            // visit访问的页面数
            visitBean.setPageVisits(pvBeansList.size());
            // 来访者的ip
            visitBean.setRemote_addr(pvBeansList.get(0).getRemote_addr());
            // 本次visit的referal
            visitBean.setReferal(pvBeansList.get(0).getReferal());
            visitBean.setSession(key.toString());

            context.write(NullWritable.get(), visitBean);

        }
    }

    public static void main(String[] args) throws Exception {
        //设置 运行job时的 用户
        System.setProperty("HADOOP_USER_NAME", "bigdata");
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(ClickStreamVisit.class);

        job.setMapperClass(ClickStreamVisitMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PageViewsBean.class);

        job.setReducerClass(ClickStreamVisitReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(VisitBean.class);

        FileInputFormat.setInputPaths(job, new Path("/weblog/pageviews/"));
        FileOutputFormat.setOutputPath(job, new Path("/weblog/visitout/"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }

}
