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

import java.util.Base64;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.events.cloud.firestore.v1.DocumentEventData;
import com.unitvectory.fileparamunit.ListFileSource;
import com.unitvectory.jsonparamunit.JsonNodeParamUnit;
import com.unitvectory.jsonparamunit.JsonParamUnitConfig;

/**
 * Test cases for FirestoreProto2Map.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class FirestoreProto2MapTest extends JsonNodeParamUnit {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new SimpleModule().addSerializer(new ByteStringSerializer())
                    .addSerializer(new DocumentReferenceSerializer()));

    private final FirestoreProto2Map firestoreProto2MapWithReference;

    private final FirestoreProto2Map firestoreProto2MapNoReference;

    public FirestoreProto2MapTest() {
        super(JsonParamUnitConfig.builder().mapper(mapper).build());
        this.firestoreProto2MapWithReference = new FirestoreProto2Map(new MockedValueToDocumentReferenceMapper());
        this.firestoreProto2MapNoReference = new FirestoreProto2Map();
    }

    @ParameterizedTest
    @ListFileSource(resources = "/tests/", fileExtension = ".json")
    void validateJSONFile(String fileName) {
        run(fileName);
    }

    @Override
    protected JsonNode process(JsonNode input, String context) {
        try {
            String protocolBuffer = input.get("protocolBuffer").asText();
            byte[] documentBytes = Base64.getDecoder().decode(protocolBuffer);
            DocumentEventData documentEventData = DocumentEventData.parseFrom(documentBytes);

            ObjectNode output = mapper.createObjectNode();

            FirestoreProto2Map firestoreProto2Map = null;
            if (input.has("valueToDocumentReferenceMapper")) {
                firestoreProto2Map = this.firestoreProto2MapWithReference;
            } else {
                firestoreProto2Map = this.firestoreProto2MapNoReference;
            }

            if (documentEventData.hasOldValue()) {
                Map<String, Object> map = firestoreProto2Map.convert(documentEventData.getOldValue());
                output.putPOJO("oldValue", map);
            }

            if (documentEventData.hasValue()) {
                Map<String, Object> map = firestoreProto2Map.convert(documentEventData.getValue());
                output.putPOJO("value", map);
            }

            return output;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
