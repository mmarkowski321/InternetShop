package model;

class GalleryImage {
    private int id;
    private String fileName;

    public GalleryImage(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public int getId() { return id; }
    public String getFileName() { return fileName; }
}