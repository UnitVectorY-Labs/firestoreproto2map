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

import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import com.google.cloud.firestore.DocumentReference;

/**
 * Conversation that utilizes Mockito to mock the conversion of a Firestore
 * DocumentReference.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class MockedValueToDocumentReferenceMapper implements ValueToDocumentReferenceMapper {

    @Override
    public DocumentReference convert(String referenceValue, String path) {
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        when(documentReference.getId()).thenReturn(referenceValue);
        return documentReference;
    }

}
