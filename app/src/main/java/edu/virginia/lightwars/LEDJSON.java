package edu.virginia.lightwars;

import java.util.Random;

/**
 * Created by askuck on 11/3/14.
 */
public class LEDJSON
{
    public final static String[] example_json = {
            "{\n" +
                "\"lights\": [\n" +
                "\n" +
                "{\"lightId\": 1, \"red\":255,\"green\":0,\"blue\":0, \"intensity\": 0.3},\n" +
                "{\"lightId\": 10, \"red\":0,\"green\":0,\"blue\":255, \"intensity\": 0.5},\n" +
                "{\"lightId\": 15, \"red\":255,\"green\":0,\"blue\":255, \"intensity\": 0.5},\n" +
                "{\"lightId\": 20, \"red\":0,\"green\":255,\"blue\":0, \"intensity\": 0.7}],\n" +
                "\n" +
                "\"propagate\": true\n" +
            "}\n",

            "{\n" +
                    "\"lights\": [\n" +
                    "\n" +
                    "{\"lightId\": 1, \"red\":222,\"green\":0,\"blue\":222, \"intensity\": 0.5},\n" +
                    "{\"lightId\": 15, \"red\":0,\"green\":222,\"blue\":0, \"intensity\": 0.5}],\n" +
                    "\n" +
                    "\"propagate\": true\n" +
             "}\n",


            "{\n" +
                    "\"lights\": [\n" +
                    "\n" +
                    "{\"lightId\": 1, \"red\":255,\"green\":255,\"blue\":255, \"intensity\": 0.9}],\n" +
                    "\n" +
                    "\"propagate\": true\n" +
            "}\n"};

    private static Random random = new Random();
    public static String getRandomJSON()
    {
        int max = example_json.length - 1;
        int min = 0;
        int ran_num = random.nextInt((max - min) + 1) + min;

        return example_json[ ran_num ];
    }

    private static String single_color =
                "{\n" +
                "\"lights\": [\n" +
                "\n" +
                "{\"lightId\": %d, \"red\":%d,\"green\":%d,\"blue\":%d, \"intensity\": 0.6}],\n" +
                "\n" +
                "\"propagate\": true\n" +
                "}\n";

    public static String getRedJSON(int x)
    {
        return String.format(single_color, x, 255, 0, 0);
    }

    public static String getGreenJSON(int x)
    {
        return String.format(single_color, x, 0, 255, 0);
    }

    public static String getBlueJSON(int x)
    {
        return String.format(single_color, x, 0, 0, 255);
    }

    public static String getWhiteJSON(int x)
    {
        return String.format(single_color, x, 255, 255, 255);
    }




}
