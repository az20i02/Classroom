package Services;



import org.springframework.stereotype.Service;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Service
public class FileStorageService {

    private static final String STORAGE_DIR = "/tmp/lms_uploads";

    public String storeFile(byte[] content, String fileName) throws IOException {
        File dir = new File(STORAGE_DIR);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName);
        FileUtils.writeByteArrayToFile(file, content);
        return file.getAbsolutePath();
    }
}
