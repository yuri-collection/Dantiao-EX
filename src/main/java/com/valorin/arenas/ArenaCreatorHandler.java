package com.valorin.arenas;

import java.util.ArrayList;
import java.util.List;

public class ArenaCreatorHandler {
    private List<ArenaCreator> acs = new ArrayList<>();
    private List<String> creators = new ArrayList<>();

    public void addAC(String creatorName) {
        acs.add(new ArenaCreator(creatorName));
        creators.add(creatorName);
    }

    public void removeAC(String creatorName) {
        acs.remove(getAC(creatorName));
        creators.remove(creatorName);
    }

    public ArenaCreator getAC(String creatorName) {
        if (acs.size() == 0) {
            return null;
        }
        for (ArenaCreator ac : acs) {
            if (ac.getCreator().equalsIgnoreCase(creatorName)) {
                return ac;
            }
        }
        return null;
    }

    public List<ArenaCreator> getACS() {
        return acs;
    }

    public List<String> getCreators() {
        return creators;
    }
}
