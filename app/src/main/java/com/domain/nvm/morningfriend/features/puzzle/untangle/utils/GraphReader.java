package com.domain.nvm.morningfriend.features.puzzle.untangle.utils;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.RawRes;
import android.util.Log;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.untangle.data.CartesianVertex;
import com.domain.nvm.morningfriend.features.puzzle.untangle.data.Untangle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class GraphReader {

    private static final String TAG = "GraphReaderLog";

    private static final String VERTICES = "vertices";
    private static final String EDGES = "edges";
    private static final String VER_NUM = "num";
    private static final String VER_X = "x";
    private static final String VER_Y = "y";

    public static Untangle getGraph(Context context, Puzzle.Difficulty difficulty) {
        String graphString = null;
        Untangle untangle = null;
        @RawRes int graphRes;
        switch (difficulty) {
            case MEDIUM:
                graphRes = R.raw.graph7_12;
                break;
            case HARD:
                graphRes = R.raw.graph8_17;
                break;
            case EASY:
            default:
                graphRes = R.raw.graph6_10;
        }
        try {
            graphString = readGraph(context, graphRes);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Error while reading untangle file", ioe);
        }
        try {
            untangle = parseGraph(graphString);
        }
        catch (JSONException je) {
            Log.e(TAG, "Error while parsing untangle string", je);
        }
        return untangle;

    }

    private static Untangle parseGraph(String jsonString) throws JSONException {
        Untangle g = new Untangle();
        JSONObject graphJson = new JSONObject(jsonString);
        JSONArray verticesJson = graphJson.getJSONArray(VERTICES);
        for (int i = 0; i < verticesJson.length(); i++) {
            JSONObject vertexJson = verticesJson.getJSONObject(i);
            int num = vertexJson.getInt(VER_NUM);
            float x = (float) vertexJson.getDouble(VER_X);
            float y = (float) vertexJson.getDouble(VER_Y);
            CartesianVertex v = new CartesianVertex(num, new PointF(x, y));
            g.addVertex(v);
        }
        JSONArray edgesJson = graphJson.getJSONArray(EDGES);
        for (int i = 0; i < edgesJson.length(); i++) {
            JSONArray edge = edgesJson.getJSONArray(i);
            int src = edge.getInt(0), dst = edge.getInt(1);
            g.connect(g.getVertex(src), g.getVertex(dst));
        }
        return g;
    }

    private static String readGraph(Context context, @RawRes int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

        }
        finally {
            is.close();
        }
        return writer.toString();
    }
}
