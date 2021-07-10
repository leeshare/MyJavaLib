package org.lixl.hadoop.lixlsource.common.fs;

/**
 * 此类包含“选项”关联到文件系统的操作
 * Created by Administrator on 1/9/2020.
 */
public final class Options {

    public static class CreateOpts {
        private CreateOpts() {
        }

        public static BlockSize blockSize(long bs) {
            return new BlockSize(bs);
        }

        public static BufferSize bufferSize(int bs) {
            return new BufferSize(bs);
        }
    }

    public static class BlockSize extends CreateOpts {
        private final long blockSize;

        protected BlockSize(long bs) {
            if (bs <= 0) {
                throw new IllegalArgumentException("Block size must be greater then 0");
            }
            blockSize = bs;
        }

        public long getValue() {
            return blockSize;
        }
    }

    public static class BufferSize extends CreateOpts {
        private final int bufferSize;

        protected BufferSize(int bs) {
            if (bs <= 0) {
                throw new IllegalArgumentException("Buffer size must be greater then 0");
            }
            bufferSize = bs;
        }

        public int getValue() {
            return bufferSize;
        }
    }

    public enum Rename {
        NONE((byte) 0),
        OVERWRITE((byte) 1),
        TO_TRASH((byte) 2);

        private final byte code;

        private Rename(byte code) {
            this.code = code;
        }

        public static Rename valueOf(byte code) {
            return code < 0 || code >= values().length ? null : values()[code];
        }

        public byte value() {
            return code;
        }
    }
}
