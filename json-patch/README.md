# json-patch

An extended implementation RFC 6902 from the IETF (also see https://github.com/java-json-tools/json-patch
& https://jsonpatch.com/). It allows for fine-grained management of the actual allowed patch operations on an entity.
To me, it seems to be rather dangerous to just accept any patch operation. Especially since some fields might be open to
change from within your application, but should not be able to be mutated by an external client.

This version relies on some external stuff to truly comply with the RFC. Namely, if a patch (batch)
fails, it should be rolled back. This is no issue when performing a single Patch. However, when
applying a list of Patches, you should always use Spring @Transactional or a comparable functionality
to make sure that you get all or nothing.

### Setup

Everything about this library revolves around two classes: the @Patchable annotation and the
PatchableEntity interface, which offers only default methods.

Every object (serializable) which you would like to open up to be able to apply patches to, will need to implement
the PatchableEntity interface. There is no need to actually write any overrides for the default methods.
Next you only need to annotate the desired patchable fields with the @Patchable annotation and
specify which operations are allowed. Here is an example:

```java

import com.compilit.core.api.annotations.Patchable;
import jakarta.json.JsonPatch.Operation;

class ExampleEntity implements PatchableEntity {

  @Patchable(allow = {Operation.REMOVE, Operation.REPLACE})
  private String myPatchableField;

  public void setMyPatchableField(String newValue) {
    this.myPatchableField = newValue;
  }

  public String getMyPatchableField() {
    return this.myPatchableField;
  }
}
```

Then you would call 'apply' on the specified entity, like so:

```java
import com.compilit.jsonpatch.PatchableEntity;
import com.github.fge.jsonpatch.JsonPatch;

class ExampleService {

    private final ExampleEntityRepository exampleEntityRepository;

    public ExampleEntity applyPatchToExampleEntity(long id, JsonPatch jsonPatch) {
        return exampleEntityRepository.findById(id)
                                      .map(entity -> enitity.apply(jsonPatch, ExampleEntity.class))
                                      .orElseThrow();
    }

    //or if you are okay by just returning the PatchableEntity:

    public PatchableEntity applyPatchToExampleEntity(long id, JsonPatch jsonPatch) {
        return exampleEntityRepository.findById(id)
                                      .map(entity -> enitity.apply(jsonPatch))
                                      .orElseThrow();
    }
}
```

### On the basic operations

Each operation needs to be understood before you can use it. So here's a brief explanation of them
all:

- REPLACE: replace the current value of the field with a new value. You cannot replace a specific
  value inside a collection. Instead, the entire collection will ben replaced.
- ADD: can be used on both simple fields and collections. It will either set the new value on the
  old value, or add the specified value to the collection. Caution should be used, since there is no
  type safety. You are responsible for adding the correct types to the collection.
- REMOVE: can be used on both simple fields and collections. In the former case it will nullify the
  value, in the latter it will remove the entry from the collection.
- COPY: copies one value to another. Will work for both collections and field values.
- MOVE: move elements from a collection from one place to another, or replace the original value
  with null, while setting the field with a new value
- TEST: test whether a path contains the specified value

### Path syntax

The path property of an operation object has slashes before each level. For example, "/animal/name".
This means that the underlying entity would have a field called animal, which is an object that
contains a field name.

Zero-based indexes are used to specify array elements. The first element of the animals array would
be at /animals/0. To add to the end of an array, use a hyphen (-) rather than an index number:
/animals/-.

### Custom ObjectMapper

The library uses a default ObjectMapper to deserialize the original JsonPatch in order to validate it.
When sending complex objects into the patch operation, for example objects containing Dates. One
might want to consider adding their own configured ObjectMapper.
If this is the case you can just override the getObjectMapper method in the PatchableEntity.
