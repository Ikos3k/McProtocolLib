package examples.proxy.util;

import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Hashtable;

public class ConnectionUtils {
    public static boolean hasConnectionError(String host, int port, int timeout) {
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
            socket.close();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static String[] getServerAddress(String p_78863_0_) {
        try {
            Hashtable<String, String> var2 = new Hashtable<>();
            var2.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            var2.put("java.naming.provider.url", "dns:");
            var2.put("com.sun.jndi.dns.timeout.retries", "1");
            InitialDirContext var3 = new InitialDirContext(var2);
            Attributes var4 = var3.getAttributes("_minecraft._tcp." + p_78863_0_, new String[]{"SRV"});
            String[] var5 = var4.get("srv").get().toString().split(" ", 4);
            return new String[]{var5[3], var5[2]};
        } catch (Throwable var6) {
            return new String[]{p_78863_0_, "25565"};
        }
    }
}
