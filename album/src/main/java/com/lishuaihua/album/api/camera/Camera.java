
package com.lishuaihua.album.api.camera;


public interface Camera<Image, Video> {

    /**
     * Take picture.
     */
    Image image();

    /**
     * Record video.
     */
    Video video();

}
