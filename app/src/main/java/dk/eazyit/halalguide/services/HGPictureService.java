package dk.eazyit.halalguide.services;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.eazyit.halalguide.HGApplication;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGCreationStatus;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGReview;
import dk.eazyit.halalguide.domain.HGUser;
import dk.eazyit.halalguide.extensions.HGQuery;

/**
 * Created by Privat on 05/08/15.
 */
public class HGPictureService {

    private static HGPictureService instance = null;

    protected HGPictureService() {
    }

    public static HGPictureService getInstance() {
        if (instance == null) {
            instance = new HGPictureService();
        }
        return instance;
    }

    public static List<HGLocationPicture> getDummyData() {

        List<HGLocationPicture> pictures = new ArrayList<>();

        HGLocationPicture pic1 = HGLocationPicture.createWithoutData(HGLocationPicture.class, "1");
        pic1.setSubmitterId("1");
        pic1.setLocationId("1");
        pic1.setCreationStatus(2);
        Bitmap bitmap = BitmapFactory.decodeResource(HGApplication.getAppContext().getResources(), R.drawable.alcohol_true);
        ParseFile file = new ParseFile("pic1", bitmap.getNinePatchChunk());
        pic1.setMediumPicture(file);
        pic1.setThumbnail(file);
        pic1.setPicture(file);

        pictures.add(pic1);
        return pictures;
    }

    public void locationPicturesForLocation(HGLocation location, final FindCallback<HGLocationPicture> callback) {

        HGQuery<HGLocationPicture> query = new HGQuery(HGLocationPicture.class);
        query.whereEqualTo("creationStatus", HGCreationStatus.Approved.getId());
        query.whereEqualTo("locationId", location.getObjectId());

        query.findInBackground(new FindCallback<HGLocationPicture>() {
            @Override
            public void done(List<HGLocationPicture> list, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground("locationPicturesForLocation", list);
                }
                callback.done(list, e);
            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("location_id", location.getObjectId());
        ParseAnalytics.trackEventInBackground("location_pictures_for_location", map);
    }

    public void locationPicturesForReview(HGReview review, final FindCallback<HGLocationPicture> callback) {
        HGQuery<HGLocationPicture> query = new HGQuery(HGLocationPicture.class);
        query.whereEqualTo("creationStatus", HGCreationStatus.Approved.getId());
        query.whereEqualTo("locationId", review.getLocationId());
        query.whereEqualTo("reviewId", review.getObjectId());
        query.setLimit(3);

        query.findInBackground(new FindCallback<HGLocationPicture>() {
            @Override
            public void done(List<HGLocationPicture> list, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground("locationPicturesForReview", list);
                }
                callback.done(list, e);
            }
        });

        Map<String, String> map = new HashMap<>();
        map.put("review_id", review.getObjectId());
        ParseAnalytics.trackEventInBackground("location_pictures_for_review", map);
    }

    public void thumbnailForLocation(HGLocation location, final FindCallback<HGLocationPicture> callback) {
        HGQuery<HGLocationPicture> query = new HGQuery(HGLocationPicture.class);
        query.whereEqualTo("creationStatus", HGCreationStatus.Approved.getId());
        query.whereEqualTo("locationId", location.getObjectId());
        query.setLimit(1);
        query.findInBackground(new FindCallback<HGLocationPicture>() {
            @Override
            public void done(List<HGLocationPicture> list, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground("thumbnailForLocation", list);
                }
                callback.done(list, e);
            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("location_id", location.getObjectId());
        ParseAnalytics.trackEventInBackground("thumbnail_for_location", map);
    }

    public void saveMultiplePictures(Uri[] uris, HGLocation location, SaveCallback saveCallback) throws IOException {

        for (int i = 0; i < uris.length; i++) {

            HGLocationPicture picture = HGLocationPicture.create(HGLocationPicture.class);
            picture.setCreationStatus(HGCreationStatus.AwaitingApproval.getId());
            picture.setLocationId(location.getObjectId());
            picture.setSubmitterId(HGUser.getCurrentUser().getObjectId());

            Uri uri = uris[i];
            String path = compressImage(uri.getPath());
            File file = new File(path);
            byte[] bytes = FileUtils.readFileToByteArray(file);
            ParseFile parseFile = new ParseFile(location.getName().replaceAll("[^A-Za-z0-9 ]", ""), bytes, "image/jpeg");

            picture.setPicture(parseFile);

            picture.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    int i = 0;
                }
            });
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = HGApplication.getAppContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
