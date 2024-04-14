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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.cloud.firestore.GeoPoint;
import com.google.events.cloud.firestore.v1.Document;
import com.google.events.cloud.firestore.v1.Value;
import com.google.type.LatLng;

/**
 * Utility to convert the Protocol Buffere Firestore Document from eventarc to a
 * represenatation of a Java Map that can be written to Firestore.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class FirestoreProto2Map {

    private ValueToDocumentReferenceMapper valueToDocumentReferenceMapper;

    /**
     * Creates a new instance of the FirestoreProto2Map class.
     * 
     * This implementation does not handle the conversion of DocumentReference and
     * those attributes will be lost.
     */
    public FirestoreProto2Map() {
        // By default the mappings are not handled
        this.valueToDocumentReferenceMapper = null;
    }

    /**
     * Creates a new instance of the FirestoreProto2Map class.
     * 
     * This implementation does not handle the conversion of DocumentReference and
     * those attributes will be lost.
     * 
     * @param valueToDocumentReferenceMapper the mapper to convert the
     *                                       DocumentReference
     */
    public FirestoreProto2Map(ValueToDocumentReferenceMapper valueToDocumentReferenceMapper) {
        this.valueToDocumentReferenceMapper = valueToDocumentReferenceMapper;
    }

    /**
     * Convert the the Firestore Document from the Protocol Buffer representation to
     * a Java Map representation that can be used by the Firestore client
     * 
     * @param document the document
     * @return the map representation of the document
     */
    public Map<String, Object> convert(Document document) {
        return convertMap(document.getFieldsMap());
    }

    /**
     * Convert the document to the map representation
     * 
     * @param document the document
     * @return the map representation of the document
     */
    private Map<String, Object> convertMap(Map<String, Value> document) {
        Map<String, Object> map = new HashMap<>();

        for (Entry<String, Value> entry : document.entrySet()) {
            String key = entry.getKey();
            Value value = entry.getValue();

            switch (value.getValueTypeCase()) {
                case ARRAY_VALUE:
                    map.put(key, convertArray(value.getArrayValue().getValuesList()));
                    break;
                case BOOLEAN_VALUE:
                    map.put(key, value.getBooleanValue());
                    break;
                case BYTES_VALUE:
                    map.put(key, value.getBytesValue());
                    break;
                case DOUBLE_VALUE:
                    map.put(key, value.getDoubleValue());
                    break;
                case GEO_POINT_VALUE:
                    map.put(key, convert(value.getGeoPointValue()));
                    break;
                case INTEGER_VALUE:
                    map.put(key, value.getIntegerValue());
                    break;
                case MAP_VALUE:
                    map.put(key, convertMap(value.getMapValue().getFieldsMap()));
                    break;
                case NULL_VALUE:
                    map.put(key, null);
                    break;
                case REFERENCE_VALUE:
                    if (this.valueToDocumentReferenceMapper != null) {
                        map.put(key, this.valueToDocumentReferenceMapper.convert(value));
                    }
                    break;
                case STRING_VALUE:
                    map.put(key, value.getStringValue());
                    break;
                case TIMESTAMP_VALUE:
                    map.put(key, convert(value.getTimestampValue()));
                    break;
                case VALUETYPE_NOT_SET:
                    // No need to convert this type
                    break;
            }
        }

        return map;
    }

    /**
     * Convert the object to the array representation
     * 
     * @param document the document
     * @return the array representation of the document
     */
    private List<Object> convertArray(
            List<Value> document) {
        List<Object> list = new ArrayList<>();
        for (Value value : document) {
            switch (value.getValueTypeCase()) {
                case ARRAY_VALUE:
                    // In Firestore arrays cannot contain arrays
                    break;
                case BOOLEAN_VALUE:
                    list.add(value.getBooleanValue());
                    break;
                case BYTES_VALUE:
                    list.add(value.getBytesValue());
                    break;
                case DOUBLE_VALUE:
                    list.add(value.getDoubleValue());
                    break;
                case GEO_POINT_VALUE:
                    list.add(convert(value.getGeoPointValue()));
                    break;
                case INTEGER_VALUE:
                    list.add(value.getIntegerValue());
                    break;
                case MAP_VALUE:
                    list.add(convertMap(value.getMapValue().getFieldsMap()));
                    break;
                case NULL_VALUE:
                    list.add(null);
                    break;
                case REFERENCE_VALUE:
                    if (this.valueToDocumentReferenceMapper != null) {
                        list.add(this.valueToDocumentReferenceMapper.convert(value));
                    }
                    break;
                case STRING_VALUE:
                    list.add(value.getStringValue());
                    break;
                case TIMESTAMP_VALUE:
                    list.add(convert(value.getTimestampValue()));
                    break;
                case VALUETYPE_NOT_SET:
                    // No need to convert this type
                    break;
            }
        }

        return list;
    }

    /**
     * Convert the Protocol Buffer representation of the LatLng to the GeoPoint
     * representation needed to set a Firestore database record
     * 
     * @param latLng the LatLng
     * @return the GeoPoint
     */
    private GeoPoint convert(LatLng latLng) {
        return new GeoPoint(latLng.getLatitude(), latLng.getLongitude());
    }

    /**
     * Convert the Protocol Buffer representation of the Timestamp to the Timestamp
     * representation needed to set a Firestore database record
     * 
     * @param timestamp the Protocol Buffer Timestamp
     * @return the Firestore Timestamp
     */
    private com.google.cloud.Timestamp convert(com.google.protobuf.Timestamp timestamp) {
        return com.google.cloud.Timestamp.ofTimeSecondsAndNanos(timestamp.getSeconds(),
                timestamp.getNanos());
    }
}
