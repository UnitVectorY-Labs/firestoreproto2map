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

import com.google.cloud.firestore.DocumentReference;

/**
 * Utility to convert the Protocol Buffere Firestore Document from eventarc to a
 * represenatation
 * that can be written to Firestore.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public interface ValueToDocumentReferenceMapper {

    /**
     * Convert the Eventarc Value reference path for a reference to a Firestore
     * DocumentReference that can be used to insert into Firestore.
     * 
     * @param referenceValue the full reference value
     * @param documentPath   the document path component from the reference value
     * @return the DocumentReference
     */
    DocumentReference convert(String referenceValue, String documentPath);
}
