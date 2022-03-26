/**
 * The Room interface, parent interface of all room types.
 */
public interface Room {

    /*
    What will a room have?

    A room will have a material it's made from

    A room will have a list of StaticFeatures, non-interactable, decorative objects that add variation to rooms
    for instance, a room may have cracks in the walls, another may have torches on its walls, yet another may have a large rug

    A room will have a list of InteractableFeatures, objects that can be interacted with in some way
    This includes secret paintings, chests, etc.

    Doors in particular are complex enough to be stored seperately from other InteractableFeatures

    */

    Door[] doors = null;

}
