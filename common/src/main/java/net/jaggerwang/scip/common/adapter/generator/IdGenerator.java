package net.jaggerwang.scip.common.adapter.generator;

import java.util.UUID;
import org.bson.types.ObjectId;

public class IdGenerator {
    public String objectId() {
        return new ObjectId().toHexString();
    }

    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
