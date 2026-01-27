package org.example.thewitcher.model.map.data;

import java.util.HashMap;
import java.util.Map;

public final class ObjectRegistry {
    private static final Map<Integer, MapObject> registry = new HashMap<>();
    static {
        registry.put(1, MapObject.ofSinglePoint(ObjectDrawings.path()));
        registry.put(2, MapObject.ofSinglePoint(ObjectDrawings.grass()));
        registry.put(10, MapObject.ofMultiPoint(ObjectDrawings.inn5x22()));
        registry.put(11, MapObject.ofMultiPoint(ObjectDrawings.workshop9x18()));
        registry.put(12, MapObject.ofMultiPoint(ObjectDrawings.wagon4x11()));
        registry.put(13, MapObject.ofMultiPoint(ObjectDrawings.cottage8x25()));
        registry.put(14, MapObject.ofMultiPoint(ObjectDrawings.camp6x14()));
        registry.put(15, MapObject.ofMultiPoint(ObjectDrawings.cabinR3x10()));
        registry.put(16, MapObject.ofMultiPoint(ObjectDrawings.cabinL3x10()));
        registry.put(17, MapObject.ofMultiPoint(ObjectDrawings.ghulNest1x5()));
        registry.put(20, MapObject.ofMultiPoint(ObjectDrawings.coniferForest10x20()));
        registry.put(21, MapObject.ofMultiPoint(ObjectDrawings.mixedForest10x20()));
        registry.put(22, MapObject.ofMultiPoint(ObjectDrawings.deciduousForest10x20()));
        registry.put(23, MapObject.ofMultiPoint(ObjectDrawings.conifer4x4()));
        registry.put(24, MapObject.ofMultiPoint(ObjectDrawings.smallConifer3x3()));
        registry.put(25, MapObject.ofMultiPoint(ObjectDrawings.tree5x6()));
        registry.put(26, MapObject.ofMultiPoint(ObjectDrawings.smallTree3x4()));
        registry.put(30, MapObject.ofSinglePoint(ObjectDrawings.player()));
        registry.put(31, MapObject.ofSinglePoint(ObjectDrawings.armorer()));
        registry.put(32, MapObject.ofSinglePoint(ObjectDrawings.blacksmith()));
        registry.put(33, MapObject.ofSinglePoint(ObjectDrawings.innkeeper()));
        registry.put(34, MapObject.ofSinglePoint(ObjectDrawings.merchant()));
        registry.put(35, MapObject.ofSinglePoint(ObjectDrawings.sorceress()));
        registry.put(36, MapObject.ofSinglePoint(ObjectDrawings.bandit()));
        registry.put(37, MapObject.ofSinglePoint(ObjectDrawings.ghul()));
        registry.put(38, MapObject.ofSinglePoint(ObjectDrawings.wolf()));
        registry.put(39, MapObject.ofSinglePoint(ObjectDrawings.ally()));
    }
    public static MapObject get(int id) { return registry.get(id); }
}
