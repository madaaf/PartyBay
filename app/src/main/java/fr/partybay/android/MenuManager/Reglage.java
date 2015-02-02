package fr.partybay.android.MenuManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import fr.partybay.android.Activity.Chargement;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;
/*
 * Created by mada on 20/10/2014.
 */
public class Reglage extends Fragment {

    private TextView pseudo = null;
    private TextView num = null;
    private TableRow numcontainer = null;
    private TableRow emailcontainer = null;
    private TableRow decocontainer = null;
    private TextView mail = null;

    private SerializeurMono<User> serializeur;
    private TextView deco;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reglage, container, false);

        pseudo = (TextView) rootView.findViewById(R.id.pseudo);
        num = (TextView) rootView.findViewById(R.id.num);
        numcontainer = (TableRow) rootView.findViewById((R.id.reglage_num_container));
        emailcontainer = (TableRow) rootView.findViewById(R.id.reglage_email_container);
        decocontainer = (TableRow) rootView.findViewById(R.id.reglage_deco_container);
        mail = (TextView) rootView.findViewById(R.id.email);
        deco = (TextView) rootView.findViewById(R.id.reglage_deco_button);

        serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        JSONObject obj = new JSONObject();
        User user;
        user = serializeur.getObject();

        pseudo.setText(user.getPseudo());
        num.setText(user.getPhone());
        mail.setText(user.getEmail());

/*
        numcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numcontainer.setBackgroundColor(getResources().getColor(R.color.white_2));
                Intent i = new Intent(getActivity(), ItemReglage.class);
                i.putExtra("activity", "num");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });*/

        numcontainer.setOnTouchListener(numListener);
        decocontainer.setOnTouchListener(decoListener);
        emailcontainer.setOnTouchListener(mailListener);
        return rootView;
    }


    View.OnTouchListener numListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    numcontainer.setBackground(getResources().getDrawable(R.drawable.menu_table));
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    numcontainer.setBackground(getResources().getDrawable(R.drawable.menu_table_border));
                    Intent i = new Intent(getActivity(), ItemReglage.class);
                    i.putExtra("activity", "num");
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    break;
            }
            return true;
        }


    };


    View.OnTouchListener decoListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    decocontainer.setBackground(getResources().getDrawable(R.drawable.menu_table));
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    decocontainer.setBackground(getResources().getDrawable(R.drawable.menu_table_border));
                    File directory = new File(getResources().getString(R.string.sdcard_path));
                    if (!directory.exists()) {
                        System.out.println("Directory does not exist.");
                        System.exit(0);
                    } else {
                        try {
                            delete(directory);

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                        Intent i = new Intent(getActivity(), Chargement.class);
                        startActivity(i);
                        // finish();
                    }
                    break;
            }
            return true;

        }
     };



    View.OnTouchListener mailListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    emailcontainer.setBackground(getResources().getDrawable(R.drawable.menu_table));
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    emailcontainer.setBackground(getResources().getDrawable(R.drawable.menu_table_border));
                    Intent i = new Intent(getActivity(), ItemReglage.class);
                    i.putExtra("activity", "email");
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    break;
            }
            return true;
        }


    };


    public static void delete(File file) throws IOException {

        if (file.isDirectory()) {
            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());

            } else {
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

}