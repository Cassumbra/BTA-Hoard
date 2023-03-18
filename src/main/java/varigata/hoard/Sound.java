package varigata.hoard;

import net.minecraft.client.Minecraft;

import java.io.*;

public class Sound {

    // Code yoinked by someone on the BTA Discord and then yoinked by me.
    public static void initAudioFile(String destination, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        try{
            in = Hoard.class.getResourceAsStream("/assets/hoard/sound/" + fileName);
            File file = new File(destination + "/" + fileName);
            file.getAbsoluteFile().getParentFile().mkdirs();
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
            out = new FileOutputStream(file);
            transferData(in, out);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            }catch (Exception e) {}
            try {
                out.close();
            }catch (Exception e) {}
        }
    }

    public static void transferData(InputStream in, OutputStream out) throws IOException {
        byte[] temp = new byte[1024];

        while(true) {
            int read = in.read(temp);
            if(read == -1) break;
            out.write(temp, 0, read);
        }
    }

    public static void registerAudioFile(String destination, String fileName, String soundPool) {
        File file = new File(destination + "\\" + fileName);
        Minecraft.getMinecraft().installResource("newsound/" + soundPool + "/" + fileName, file);
    }
}
