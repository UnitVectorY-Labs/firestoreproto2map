/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.unitvectory.firestoreproto2map;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.cloud.firestore.Blob;

/**
 * Customized serializer for the Blob class for conversation to JSON to
 * match the test cases.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class BlobSerializer extends StdSerializer<Blob> {

    public BlobSerializer() {
        super(Blob.class);
    }

    @Override
    public void serialize(Blob value, JsonGenerator jsonGenerator,
            SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("_byteString",
                Base64.getEncoder().encodeToString(value.toBytes()));
        jsonGenerator.writeEndObject();
    }
}
