package io.github.bucket4j.distributed.remote;

import io.github.bucket4j.distributed.serialization.DeserializationAdapter;
import io.github.bucket4j.distributed.serialization.SerializationAdapter;
import io.github.bucket4j.distributed.serialization.SerializationHandle;
import io.github.bucket4j.distributed.versioning.Version;
import io.github.bucket4j.distributed.versioning.Versions;
import io.github.bucket4j.util.ComparableByContent;

import java.io.IOException;

import static io.github.bucket4j.distributed.versioning.Versions.v_5_0_0;

public class RemoteStat implements ComparableByContent<RemoteStat> {

    private long consumedTokens;

    public RemoteStat(long consumedTokens) {
        this.consumedTokens = consumedTokens;
    }

    public long getConsumedTokens() {
        return consumedTokens;
    }

    public void addConsumedTokens(long consumedTokens) {
        this.consumedTokens += consumedTokens;
    }

    public static final SerializationHandle<RemoteStat> SERIALIZATION_HANDLE = new SerializationHandle<RemoteStat>() {
        @Override
        public <S> RemoteStat deserialize(DeserializationAdapter<S> adapter, S input, Version backwardCompatibilityVersion) throws IOException {
            int formatNumber = adapter.readInt(input);
            Versions.check(formatNumber, v_5_0_0, v_5_0_0);

            long consumedTokens = adapter.readLong(input);
            return new RemoteStat(consumedTokens);
        }

        @Override
        public <O> void serialize(SerializationAdapter<O> adapter, O output, RemoteStat stat, Version backwardCompatibilityVersion) throws IOException {
            adapter.writeInt(output, v_5_0_0.getNumber());

            adapter.writeLong(output, stat.consumedTokens);
        }

        @Override
        public int getTypeId() {
            return 6;
        }

        @Override
        public Class<RemoteStat> getSerializedType() {
            return RemoteStat.class;
        }

    };

    public RemoteStat copy() {
        return new RemoteStat(consumedTokens);
    }

    @Override
    public boolean equalsByContent(RemoteStat other) {
        return consumedTokens == other.consumedTokens;
    }

}