public class RoomCandidate {

    String[] tags;

    // accepts any number of strings as tags
    public RoomCandidate(String... tags) {
        this.tags = tags;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String... tags) {
        this.tags = tags;
    }
}
