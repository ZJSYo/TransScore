import os
import sys
import shutil

def delete_folders(path):
    # 获取目录下的所有文件和文件夹
    items = os.listdir(path)

    for item in items:
        item_path = os.path.join(path, item)

        if os.path.isdir(item_path):  # 如果是文件夹
            delete_folders(item_path)  # 递归调用自身来删除子文件夹
            os.rmdir(item_path)  # 删除空文件夹
        else:
            os.remove(item_path)  # 如果是文件，直接删除
            
def rename_and_move_files(root_folder, dest_folder, new_filename):
    # 遍历根文件夹下的所有子文件夹
    for foldername, subfolders, filenames in os.walk(root_folder):
        for filename in filenames:
            # 判断文件是否为wav文件
            if filename.endswith(".wav"):
                # 获取文件的完整路径
                file_path = os.path.join(foldername, filename)

                # 获取文件的扩展名
                file_extension = os.path.splitext(filename)[1]

                # 构造新的文件名
                new_file_name = new_filename + file_extension

                # 构造新的文件路径
                new_file_path = os.path.join(dest_folder, new_file_name)

                # 复制文件
                shutil.copy(file_path, new_file_path)

                # 删除原始文件
                os.remove(file_path)

def rename_files(folder_path, current_ext, new_ext):
    # 获取文件夹下的所有文件
    files = os.listdir(folder_path)

    for file in files:
        # 检查文件后缀
        if file.endswith(current_ext):
            # 构建新的文件名
            new_filename = os.path.splitext(file)[0] + new_ext
            # 重命名文件
            os.rename(os.path.join(folder_path, file), os.path.join(folder_path, new_filename))
            print(f'Renamed {file} to {new_filename}')

if __name__ == '__main__':
    
    
    text=sys.argv[1]
    filename_with_extension = os.path.basename(text)

    # 提取文件名（不包含扩展名）
    filename_without_extension = os.path.splitext(filename_with_extension)[0]
    # print(filename_without_extension)
    with open(text, 'r') as file:
        text = file.read()
        # print(text)
    cmd=f'audioldm2 --model_name "audioldm_48k" --device cuda -t "Musical {text}"'
    os.system(cmd)
    

    # 指定文件夹路径
    root_folder = "/home/zhang/星辰大海/piano_transcription/output"

    # 指定目标文件夹路径
    dest_folder = "/home/zhang/下载/软件工程/mp3_output"


    rename_and_move_files(root_folder, dest_folder, filename_without_extension)
    
    folder_path = "/home/zhang/星辰大海/piano_transcription/output"
    delete_folders(folder_path)
    folder_path = '/home/zhang/下载/软件工程/mp3_output'
    current_ext = '.wav'
    new_ext = '.mp3'

    # 调用函数重命名文件
    rename_files(folder_path, current_ext, new_ext)