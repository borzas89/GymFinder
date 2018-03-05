package hu.zsoltborza.gymfinderhun.utils;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.model.GymData;
import hu.zsoltborza.gymfinderhun.model.GymListItem;


/**
 * Created by Borzas on 2017. 02. 20..
 */

public class Utils {

    /**
     * Reading gyms from file, returning as a list.
     * @param context
     * @return List of questions
     */
    public static List<GymListItem> getDataFromFile(Context context){

        Resources res = context.getResources();

        StringBuilder builder = new StringBuilder();
        InputStream is = res.openRawResource(R.raw.gym_hun_new);
        Scanner scanner = new Scanner(is);

        while (scanner.hasNextLine()){
            builder.append(scanner.nextLine());
        }

        String file = (builder.toString());

        Gson gson = new Gson();

        GymData gymList = gson.fromJson(file,GymData.class);

        return gymList.getGymList();
    }



}
