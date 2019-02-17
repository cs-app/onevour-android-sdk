package org.cise.core.utilities.dummy;

import java.util.ArrayList;
import java.util.List;

public class DummyHelper {

    public static List<String> initListString() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("Sample " + i);
        }
        return list;
    }
}
