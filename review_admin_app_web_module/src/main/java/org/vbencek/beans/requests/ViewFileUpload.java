/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.requests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.file.UploadedFile;
import org.vbencek.properties.PropertiesLoader;

@Named(value = "viewFileUpload")
@RequestScoped
public class ViewFileUpload {

    private UploadedFile file;
    private String uploadLocation;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void handleFileUpload() {
        if (file != null) {
            loadUploadLocation();
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            try {
                copyFile(file.getFileName(), file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUploadLocation() {
        PropertiesLoader propLoader = new PropertiesLoader();
        uploadLocation = propLoader.getProperty("admin.bookDetails.uploadFolder");

    }

    public void copyFile(String fileName, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(uploadLocation + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("FILE UPLOADER: " + fileName + " succesfully uploaded");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
