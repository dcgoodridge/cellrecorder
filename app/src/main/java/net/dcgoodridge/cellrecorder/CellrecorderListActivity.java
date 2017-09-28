package net.dcgoodridge.cellrecorder;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CellrecorderListActivity extends AppCompatActivity {

    private FileListGenericAdapter adapter;
    private TextView emptylistLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellrecorder_list);

        adapter = new FileListGenericAdapter(this, new ArrayList<File>());
        refreshList();

        ListView my_listview = (ListView) findViewById(R.id.nmearecorder_list_listview);
        my_listview.setEmptyView(findViewById(R.id.nmearecorder_list_empty_view));
        my_listview.setAdapter(adapter);

        emptylistLink = (TextView) findViewById(R.id.nmearecorder_list_emptylist_link);
        emptylistLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CellrecorderListActivity.this, CellrecorderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final File cellFileSelected = adapter.getItem(position);
                final Dialog dialog = new Dialog(CellrecorderListActivity.this, R.style.AppCompatAlertDialogStyle);
                dialog.setContentView(R.layout.dialog_cellrecorder_list_click);

                LinearLayout llOpen = (LinearLayout) dialog.findViewById(R.id.dialog_nmearecorder_list_click_open);
                LinearLayout llShare = (LinearLayout) dialog.findViewById(R.id.dialog_nmearecorder_list_click_share);
                LinearLayout llDelete = (LinearLayout) dialog.findViewById(R.id.dialog_nmearecorder_list_click_delete);
                TextView tvName = (TextView) dialog.findViewById(R.id.dialog_nmearecorder_list_click_name);
                tvName.setText(cellFileSelected.getName());

                llOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startOpenFile(cellFileSelected);
                        dialog.dismiss();
                    }
                });

                llShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startShareFile(cellFileSelected);
                        dialog.dismiss();
                    }
                });

                llDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCellFile(cellFileSelected);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }


    private void deleteCellFile(File cellFile) {
        boolean deleted = cellFile.delete();
        if (!deleted) Toast.makeText(this, "File could not be deleted", Toast.LENGTH_SHORT).show();
        refreshList();
    }

    private void refreshList() {
        adapter.clear();
        adapter.addAll(computeCellFileListOrdered());
    }


    private void startOpenFile(File file) {
        final Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authority_fileprovider), file);
        Intent openIntent = new Intent();
        openIntent.setAction(Intent.ACTION_VIEW);
        openIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openIntent.setDataAndType(contentUri, "text/plain");
        startActivity(openIntent);
    }


    private void startShareFile(File file) {
        final Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authority_fileprovider), file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        String chooserTitle = "Compartir " + file.getName();
        startActivity(Intent.createChooser(shareIntent, chooserTitle));
    }


    private List<File> computeFileList() {
        List<File> fileList = new ArrayList();

        File recordFolder = new File(
                getFilesDir().getAbsolutePath() + File.separator + CellrecorderService.INTERNAL_FOLDER_CELL);
        if (!recordFolder.exists()) {
            recordFolder.mkdirs();
        }

        File[] listOfFiles = recordFolder.listFiles();
        if (listOfFiles == null) return fileList;

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fileList.add(listOfFiles[i]);
            }
        }
        return fileList;
    }

    private List<File> computeCellFileList() {
        List<File> fileList = computeFileList();
        return fileList;
    }

    private int longCompare(long lhs, long rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    private List<File> computeCellFileListOrdered() {
        List<File> cellFiles = computeCellFileList();
        Collections.sort(cellFiles, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return longCompare(f2.lastModified(), f1.lastModified());
            }
        });
        return cellFiles;
    }


    private class CellFile implements Comparable<CellFile> {

        private File file;

        public CellFile(File file) {
            this.file = file;
        }

        public File getFile() {
            return file;
        }

        @Override
        public int compareTo(CellFile another) {
            long lastModified = file.lastModified();
            long anotherLastModified = another.getFile().lastModified();
            if (lastModified > anotherLastModified) {
                return -1;
            } else if (lastModified < anotherLastModified) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
