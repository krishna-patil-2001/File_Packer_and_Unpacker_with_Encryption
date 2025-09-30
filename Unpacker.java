import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class EncryptionHelper {
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MarvellousKey123".getBytes(); // 16-byte key

    public static byte[] decrypt(byte[] data) throws Exception {
        SecretKey secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
}

class Unpacker {
    public static void main(String[] args) {
        JFrame fobj = new JFrame("Marvellous Unpacker");

        fobj.setSize(400, 300);
        fobj.setLayout(null);
        fobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jobj = new JLabel("File Name");
        jobj.setBounds(50, 50, 100, 30);

        JTextField tobj = new JTextField();
        tobj.setBounds(130, 50, 130, 30);

        JButton bobj = new JButton("Unpack");
        bobj.setBounds(140, 100, 100, 30);

        JLabel robj = new JLabel("");
        robj.setBounds(50, 200, 300, 30);

        bobj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aobj) {
                String Filename = tobj.getText();

                if (Filename.isEmpty()) {
                    robj.setText("Please enter a file name");
                } else {
                    File fobj = new File(Filename);

                    if (!fobj.exists()) {
                        robj.setText("File not found");
                        robj.setForeground(Color.RED);
                        return;
                    }

                    try {
                        String Header = null;
                        File fobjnew = null;
                        int FileSize = 0, iRet = 0;

                        FileInputStream fiobj = new FileInputStream(fobj);
                        byte HeaderBuffer[] = new byte[100];

                        while ((iRet = fiobj.read(HeaderBuffer, 0, 100)) != -1) {
                            Header = new String(HeaderBuffer);
                            Header = Header.trim();

                            String Tokens[] = Header.split(" ");
                            if (Tokens.length < 2) break;

                            fobjnew = new File(Tokens[0]);
                            fobjnew.createNewFile();

                            FileSize = Integer.parseInt(Tokens[1]);

                            byte Buffer[] = new byte[FileSize];
                            fiobj.read(Buffer, 0, FileSize);

                            // 🔐 Decrypt the data
                            byte[] decryptedData = EncryptionHelper.decrypt(Buffer);

                            FileOutputStream foobj = new FileOutputStream(fobjnew);
                            foobj.write(decryptedData);
                            foobj.close();

                            robj.setText("File Unpacked Successfully... " );
                            robj.setForeground(Color.BLUE);
                        }

                        fiobj.close();
                    } catch (Exception eobj) {
                        robj.setText("Error: " + eobj.getMessage());
                        robj.setForeground(Color.RED);
                    }
                }
            }
        });

        fobj.add(jobj);
        fobj.add(tobj);
        fobj.add(bobj);
        fobj.add(robj);

        fobj.setVisible(true);
    }
}

