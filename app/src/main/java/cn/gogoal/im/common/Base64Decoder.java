package cn.gogoal.im.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Base64Decoder extends FilterInputStream {
    private static final char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final int[] ints = new int[128];
    private int charCount;
    private int carryOver;

    public Base64Decoder(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        int x;
        do {
            x = this.in.read();
            if(x == -1) {
                return -1;
            }
        } while(Character.isWhitespace((char)x));

        ++this.charCount;
        if(x == 61) {
            return -1;
        } else {
            x = ints[x];
            int mode = (this.charCount - 1) % 4;
            if(mode == 0) {
                this.carryOver = x & 63;
                return this.read();
            } else {
                int decoded;
                if(mode == 1) {
                    decoded = (this.carryOver << 2) + (x >> 4) & 255;
                    this.carryOver = x & 15;
                    return decoded;
                } else if(mode == 2) {
                    decoded = (this.carryOver << 4) + (x >> 2) & 255;
                    this.carryOver = x & 3;
                    return decoded;
                } else if(mode == 3) {
                    decoded = (this.carryOver << 6) + x & 255;
                    return decoded;
                } else {
                    return -1;
                }
            }
        }
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        if(buf.length < len + off - 1) {
            throw new IOException("The input buffer is too small: " + len + " bytes requested starting at offset " + off + " while the buffer " + " is only " + buf.length + " bytes long.");
        } else {
            int i;
            for(i = 0; i < len; ++i) {
                int x = this.read();
                if(x == -1 && i == 0) {
                    return -1;
                }

                if(x == -1) {
                    break;
                }

                buf[off + i] = (byte)x;
            }

            return i;
        }
    }

    public static String decode(String encoded) {
        return new String(decodeToBytes(encoded));
    }

    public static byte[] decodeToBytes(String encoded) {
        byte[] bytes = null;

        try {
            bytes = encoded.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var6) {
            ;
        }

        Base64Decoder in = new Base64Decoder(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream out = new ByteArrayOutputStream((int)((double)bytes.length * 0.67D));

        try {
            byte[] ignored = new byte[4096];

            int bytesRead;
            while((bytesRead = in.read(ignored)) != -1) {
                out.write(ignored, 0, bytesRead);
            }

            out.close();
            return out.toByteArray();
        } catch (IOException var7) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Usage: java Base64Decoder fileToDecode");
        } else {
            Base64Decoder decoder = null;

            try {
                decoder = new Base64Decoder(new BufferedInputStream(new FileInputStream(args[0])));
                byte[] buf = new byte[4096];

                int bytesRead;
                while((bytesRead = decoder.read(buf)) != -1) {
                    System.out.write(buf, 0, bytesRead);
                }
            } finally {
                if(decoder != null) {
                    decoder.close();
                }

            }

        }
    }

    static {
        for(int i = 0; i < 64; ints[chars[i]] = i++) {
            ;
        }

    }
}

