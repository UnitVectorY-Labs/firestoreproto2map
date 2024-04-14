[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![codecov](https://codecov.io/gh/UnitVectorY-Labs/firestoreproto2map/graph/badge.svg?token=1PXHW608HT)](https://codecov.io/gh/UnitVectorY-Labs/firestoreproto2map)

# firestoreproto2map

Helper library to convert Firestore Protocol Buffer from event to map that can be used by Firestore

## Purpose

This library takes the Protocol Buffer sent from Firestore for a document and converts it to a Java Map Object. Firestore stores the underlying documents as Protocol Buffers and therefore that is what is sent to a Cloud Function when subscribed. If you want to take this and insert it back into Firestore it must be converted to a compatible object, that is what this library does.

## Getting Started

This library requires Java 17.

This library is still under development.

## Usage

```java
package functions;

import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.firestore.v1.DocumentEventData;
import com.google.protobuf.InvalidProtocolBufferException;
import com.unitvectory.firestoreproto2map.FirestoreProto2Map;
import io.cloudevents.CloudEvent;
import java.util.logging.Logger;

public class FirebaseFirestore implements CloudEventsFunction {
  private static final Logger logger = Logger.getLogger(FirebaseFirestore.class.getName());

  @Override
  public void accept(CloudEvent event) throws InvalidProtocolBufferException {
    DocumentEventData firestorEventData = DocumentEventData.parseFrom(event.getData().toBytes());
    
    // This will not handle DocumentReference attributes
    FirestoreProto2Map firestoreProto2Map = new FirestoreProto2Map();

    if (documentEventData.hasOldValue()) {
      Map<String, Object> oldValueMap = firestoreProto2Map.convert(documentEventData.getOldValue());

      // Map compatible to be insert into Firestore
    }

    if (documentEventData.hasValue()) {
      Map<String, Object> valueMap = firestoreProto2Map.convert(documentEventData.getValue());

      // Map compatible to be insert into Firestore
    }
  }
}
```

## Reference Limitation

Firestore supports reference documents, these are handled different from the other field types and require a `DocumentReference` object which requires the Firestore SDK to get that object.
