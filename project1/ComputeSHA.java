import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
public class ComputeSHA{
        public static void main(String [] args) throws NoSuchAlgorithmException,IOException{
		if(args.length!=1){
                        System.out.println("Please input only ONE file name");
                }
		else{
			String filename=args[0];
                	System.out.println(sha1(readfile(filename)));
		}
        }
        static String sha1 (byte[] read_content) throws NoSuchAlgorithmException{
                MessageDigest mDigest=MessageDigest.getInstance("SHA1");
                byte[] result=mDigest.digest(read_content);
                return bytes_To_Hex(result);
        }
        final protected static char[] hex_array = "0123456789abcdef".toCharArray();
        public static String bytes_To_Hex(byte[] bytes) {
                 char[] hex_chars = new char[bytes.length*2];
                 for (int i=0; i<bytes.length; i++) {
                        int u = bytes[i]&0xFF;
                        hex_chars[i*2]=hex_array[u>>>4];
                        hex_chars[i*2+1]=hex_array[u&0x0F];
        }
                 return new String(hex_chars);
        }
	public static byte[] readfile(String filename) throws IOException
        {
		return readAllBytes(java.nio.file.Paths.get(filename));
	}
}
