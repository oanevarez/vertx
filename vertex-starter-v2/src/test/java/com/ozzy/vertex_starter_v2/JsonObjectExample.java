package com.ozzy.vertex_starter_v2;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class JsonObjectExample {

  @Test
  void jsonObjectCanBeMapped(){
    final JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id", 1);
    myJsonObject.put("name", "Alice");
    myJsonObject.put("loves_vertx", true);

    //assertEquals("", myJsonObject.encode());

    assertEquals("{\"id\":1,\"name\":\"Alice\",\"loves_vertx\":true}", myJsonObject.encode());

  }

  @Test
  void jsonObjectCanBeMapped_Two(){
    final JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id", 1);
    myJsonObject.put("name", "Alice");
    myJsonObject.put("loves_vertx", true);

    //assertEquals("", myJsonObject.encode());
    assertNotEquals("", myJsonObject.encode());
    //assertEquals("{\"id\":1,\"name\":\"Alice\",\"loves_vertx\":true}", myJsonObject.encode());

  }

  @Test
  void jsonObjectCanBeMapped_Three(){
    final JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id", 1);
    myJsonObject.put("name", "Alice");
    myJsonObject.put("loves_vertx", true);

    final String encoded = myJsonObject.encode();
    //assertEquals("{\"id\":1,\"name\":\"Alice\",\"loves_vertx\":true}", myJsonObject.encode());

    final JsonObject decodedJsonObject = new JsonObject(encoded);
    assertEquals(myJsonObject, decodedJsonObject);

  }

  @Test
  void jsonObjectCanBeCreatedFromMap(){
    final Map<String, Object> myMap = new HashMap<>();
    myMap.put("id", 1);
    myMap.put("name", "Alice");
    myMap.put("loves_vertx", true);

    final JsonObject asJson = new JsonObject(myMap);
    assertEquals(myMap, asJson.getMap());
  }

  @Test
  void jsonObjectCanBeCreatedFromMap_Two(){
    final Map<String, Object> myMap = new HashMap<>();
    myMap.put("id", 1);
    myMap.put("name", "Alice");
    myMap.put("loves_vertx", true);

    final JsonObject asJson = new JsonObject(myMap);

    assertEquals(1, asJson.getInteger("id"));
    assertEquals("Alice", asJson.getString("name"));
    assertEquals(true, asJson.getBoolean("loves_vertx"));
  }

  @Test
  void jsonArrayCanBeMapped() {
    final JsonArray myJasonArray = new JsonArray();
    myJasonArray
      .add(new JsonObject().put("id", 1)
        .put("name", "Ozzy"))
      .add(new JsonObject().put("id", 2)
        .put("name", "Osvaldo"))
      .add(new JsonObject().put("id", 3)
        .put("name", "Baldo"));

    assertEquals("[{\"id\":1,\"name\":\"Ozzy\"},{\"id\":2,\"name\":\"Osvaldo\"},{\"id\":3,\"name\":\"Baldo\"}]", myJasonArray.encode());

  }

  @Test
  void canMapJavaObjects(){

    final Person person = new Person(1, "Alice", true);
    final JsonObject alice = JsonObject.mapFrom(person);

    assertEquals(person.getId(), alice.getInteger("id"));
    assertEquals(person.getName(), alice.getString("name"));
    assertEquals(person.isLovesVertx(), alice.getBoolean("lovesVertx"));

    final Person person2 = alice.mapTo(Person.class);
    assertEquals(person.getId(), person2.getId());
    assertEquals(person.getName(), person2.getName());
    assertEquals(person.isLovesVertx(), person2.isLovesVertx());
  }



}
