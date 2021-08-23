package org.cise.core.utilities.helper;

import java.util.ArrayList;
import java.util.List;

public class DummyHelper {

    public static List<String> initListString(int maxLoop) {
        if (0 > maxLoop) maxLoop = 10;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < maxLoop; i++) {
            list.add("Sample " + i);
        }
        return list;
    }
}
