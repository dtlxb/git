package cn.gogoal.im.common;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Base64Encoder extends FilterOutputStream {
    private static final char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private int charCount;
    private int carryOver;

    public Base64Encoder(OutputStream out) {
        super(out);
    }

    public void write(int b) throws IOException {
        if(b < 0) {
            b += 256;
        }

        int lookup;
        if(this.charCount % 3 == 0) {
            lookup = b >> 2;
            this.carryOver = b & 3;
            this.out.write(chars[lookup]);
        } else if(this.charCount % 3 == 1) {
            lookup = (this.carryOver << 4) + (b >> 4) & 63;
            this.carryOver = b & 15;
            this.out.write(chars[lookup]);
        } else if(this.charCount % 3 == 2) {
            lookup = (this.carryOver << 2) + (b >> 6) & 63;
            this.out.write(chars[lookup]);
            lookup = b & 63;
            this.out.write(chars[lookup]);
            this.carryOver = 0;
        }

        ++this.charCount;
        if(this.charCount % 57 == 0) {
            this.out.write(10);
        }

    }

    public void write(byte[] buf, int off, int len) throws IOException {
        for(int i = 0; i < len; ++i) {
            this.write(buf[off + i]);
        }

    }

    public void close() throws IOException {
        int lookup;
        if(this.charCount % 3 == 1) {
            lookup = this.carryOver << 4 & 63;
            this.out.write(chars[lookup]);
            this.out.write(61);
            this.out.write(61);
        } else if(this.charCount % 3 == 2) {
            lookup = this.carryOver << 2 & 63;
            this.out.write(chars[lookup]);
            this.out.write(61);
        }

        super.close();
    }

    public static String encode(String unencoded) {
        byte[] bytes = null;

        try {
            bytes = unencoded.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            ;
        }

        return encode(bytes);
    }

    public static String encode(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream((int)((double)bytes.length * 1.37D));
        Base64Encoder encodedOut = new Base64Encoder(out);

        try {
            encodedOut.write(bytes);
            encodedOut.close();
            return out.toString("UTF-8");
        } catch (IOException var4) {
            return null;
        }
    }
}
