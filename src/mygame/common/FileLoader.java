package mygame.common;

import java.io.IOException;
import java.io.InputStream;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;

/**
 * Loads an inputStream
 * 
 * @author Death
 *
 */
public class FileLoader implements AssetLoader {

	@Override
	public InputStream load(AssetInfo assetInfo) throws IOException {
		InputStream openStream = assetInfo.openStream();
		return openStream;
	}

}
