
package com.lishuaihua.album.impl;

import com.lishuaihua.album.AlbumFile;

import java.util.ArrayList;


public interface AlbumCallback {

    /**
     * Photo album callback selection result.
     *
     * @param albumFiles file path list.
     */
    void onAlbumResult(ArrayList<AlbumFile> albumFiles);

    /**
     * The album canceled the operation.
     */
    void onAlbumCancel();


}