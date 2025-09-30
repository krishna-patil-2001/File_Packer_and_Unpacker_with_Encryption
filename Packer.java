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

    public static byte[] encrypt(byte[] data) throws Exception {
        SecretKey secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
}

class Packer {
    public static void main(String[] args) {
        JFrame fobj = new JFrame("Marvellous Packer");

        fobj.setSize(400, 300);
        fobj.setLayout(null);
        fobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jobj1 = new JLabel("Folder Name");
        jobj1.setBounds(50, 50, 100, 30);

        JTextField tobj1 = new JTextField();
        tobj1.setBounds(130, 50, 130, 30);

        JLabel jobj2 = new JLabel("File Name");
        jobj2.setBounds(50, 100, 100, 30);

        JTextField tobj2 = new JTextField();
        tobj2.setBounds(130, 100, 130, 30);

        JButton bobj = new JButton("Pack");
        bobj.setBounds(140, 150, 100, 30);

        JLabel robj = new JLabel("");
        robj.setBounds(50, 200, 300, 30);

        bobj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aobj) {
                try {
                    String FolderName = tobj1.getText();
                    File fobj = new File(FolderName);

                    if (FolderName.isEmpty()) {
                        robj.setText("Please enter a Directory name");
                        robj.setForeground(Color.darkGray);
                    } else if (fobj.exists() && fobj.isDirectory()) {
                        robj.setText("Please enter file name");
                        robj.setForeground(Color.DARK_GRAY);

                        String FileName = tobj2.getText();
                        File Packobj = new File(FileName);

                        boolean bRet = Packobj.createNewFile();
                        if (bRet == false) {
                            robj.setText("Unable to create pack file");
                            robj.setForeground(Color.RED);
                            return;
                        }

                        File Arr[] = fobj.listFiles();
                        FileOutputStream foobj = new FileOutputStream(Packobj);

                        for (File file : Arr) {
                            // Read file fully
                            FileInputStream fiobj = new FileInputStream(file);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte Buffer[] = new byte[1024];
                            int iRet;
                            while ((iRet = fiobj.read(Buffer)) != -1) {
                                baos.write(Buffer, 0, iRet);
                            }
                            fiobj.close();

                            // Encrypt file content
                            byte[] encryptedData = EncryptionHelper.encrypt(baos.toByteArray());

                            // Prepare header (file name + size)
                            String Header = file.getName() + " " + encryptedData.length;
                            while (Header.length() < 100) {
                                Header = Header + " ";
                            }
                            foobj.write(Header.getBytes());

                            // Write encrypted data
                            foobj.write(encryptedData);

                            robj.setText("Packed: " + file.getName());
                        }

                        foobj.close();
                        robj.setText("Packing with Encryption Done...");
                        robj.setForeground(Color.BLUE);
                    } else {
                        robj.setText("Directory not found");
                        robj.setForeground(Color.RED);
                    }
                } catch (Exception eobj) {
                    robj.setText("Error: " + eobj.getMessage());
                    robj.setForeground(Color.RED);
                }
            }
        });

        fobj.add(jobj1);
        fobj.add(tobj1);
        fobj.add(jobj2);
        fobj.add(tobj2);
        fobj.add(bobj);
        fobj.add(robj);

        fobj.setVisible(true);
    }
}

