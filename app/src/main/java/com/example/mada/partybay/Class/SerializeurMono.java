package com.example.mada.partybay.Class;

/**
 * Created by mada on 06/11/2014.
 */

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeurMono<Objet> {
    private File fichier;
    private FileInputStream fileInput;
    private FileOutputStream fileOutput;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;

    public SerializeurMono(String filepath) {
        Log.d("fichier", filepath);
        fichier = new File(filepath);
        try {
            fichier.createNewFile();
        } catch (IOException e) {
            System.out.println("Err de creation de fichier : "+e.getMessage());
        }
    }

    public void setObjet(Objet o){
        try {
            fileOutput = new FileOutputStream(fichier, false);
            objectOutput = new ObjectOutputStream(fileOutput);

            try {
                objectOutput.writeObject(o);
                objectOutput.flush();
            } finally {
                try {
                    objectOutput.close();
                } finally {
                    fileOutput.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur serializer : "+e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Objet getObject(){
        Objet obj = null;
        try {
            fileInput = new FileInputStream(fichier);
            objectInput = new ObjectInputStream(fileInput);

            try {
                obj = (Objet) objectInput.readObject();
            } finally {
                try {
                    objectInput.close();
                } finally {
                    fileInput.close();
                }
            }
        } catch(IOException e) {
            System.out.println("Err deserializer1 : "+e.getMessage());
        } catch(ClassNotFoundException e1) {
            System.out.println("Err deserializer2 : "+e1.getMessage());
        }

        if(obj==null)
            return null;

        return obj;
    }
}
