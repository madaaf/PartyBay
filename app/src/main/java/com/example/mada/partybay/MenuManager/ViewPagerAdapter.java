package com.example.mada.partybay.MenuManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mada.partybay.Activity.Reglage;
import com.example.mada.partybay.R;

/**
 * Created by mada on 06/01/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.d("je suis la ","ok");
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos){
            case 0 : {
                Log.d("postion", String.valueOf(pos));
                return new Reglage();
            }
            case 1 : {
                Log.d("postion", String.valueOf(pos));
                return new getReglage();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
/*
    public static class getActivite extends Fragment{
        private TextView pseudo = null;
        private TextView num = null;
        private TextView mail = null;

        private SerializeurMono<User> serializeur;
        private Button deco;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.reglage, container, false);

            pseudo = (TextView) rootView.findViewById(R.id.pseudo);
            num = (TextView) rootView.findViewById(R.id.num);
            mail = (TextView) rootView.findViewById(R.id.email);
            deco = (Button) rootView.findViewById(R.id.reglage_deco_button);

            serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
            JSONObject obj = new JSONObject();
            User user;
            user = serializeur.getObject();

            pseudo.setText(user.getPseudo());
            num.setText(user.getPhone());
            mail.setText(user.getEmail());

            deco.setOnClickListener(decoListener);
            return  rootView;
        }


        View.OnClickListener decoListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File directory =new File(getResources().getString(R.string.sdcard_path));

                if(!directory.exists()){
                    System.out.println("Directory does not exist.");
                    System.exit(0);
                }else{
                    try{

                        delete(directory);

                    }catch(IOException e){
                        e.printStackTrace();
                        System.exit(0);
                    }

                    //Intent i = new Intent(Reglage.this,Chargement.class);
                    //startActivity(i);
                   // finish();
                }

            }
        };

        public static void delete(File file) throws IOException{

            if(file.isDirectory()){
                //directory is empty, then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());

                }else{
                    //list all the directory contents
                    String files[] = file.list();
                    for (String temp : files) {
                        //construct the file structure
                        File fileDelete = new File(file, temp);

                        //recursive delete
                        delete(fileDelete);
                    }

                    //check the directory again, if empty then delete it
                    if(file.list().length==0){
                        file.delete();
                        System.out.println("Directory is deleted : " + file.getAbsolutePath());
                    }
                }

            }else{
                //if file, then delete it
                file.delete();
                System.out.println("File is deleted : " + file.getAbsolutePath());
            }
        }

    }

*/
    public static class getReglage extends Fragment{
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profile, container, false);
            return  rootView;
        }

    }
}
