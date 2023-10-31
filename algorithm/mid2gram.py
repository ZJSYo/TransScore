import os
import sys
import shutil
from piano_transcription_inference import PianoTranscription, sample_rate, load_audio

def generate_mid_file(mp3_file_path, mid_file_path):
    # Load audio
    audio, _ = load_audio(mp3_file_path, sr=sample_rate, mono=True)

    # Transcriptor
    transcriptor = PianoTranscription(device='cuda')    # 'cuda' | 'cpu'

    # Transcribe and write out to MIDI file
    transcribed_dict = transcriptor.transcribe(audio, mid_file_path)

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: python mid2gram.py mp3_file_path")
        sys.exit(1)
    
    mp3_file_path = sys.argv[1]
    mid_file_path = '/home/zhang/下载/软件工程/mid_output'
    filename_with_extension = os.path.basename(mp3_file_path)

    # 提取文件名（不包含扩展名）
    filename_without_extension = os.path.splitext(filename_with_extension)[0]
    # print(filename_without_extension)

    new_file_path = os.path.join(mid_file_path, filename_without_extension + '.mid')
    
    
    generate_mid_file(mp3_file_path, new_file_path)

    # Run sheet.exe with wine
    cmd = f'cd /home/zhang/星辰大海/MidiToSheetMusic && wine sheet.exe {new_file_path} {filename_without_extension}'
    
    os.system(cmd)
    source_dir = '/home/zhang/星辰大海/MidiToSheetMusic'

    # Destination directory
    destination_dir = '/home/zhang/下载/软件工程/png_output'

    for filename in os.listdir(source_dir):
        if filename.endswith('.png'):

            # 构建新文件名
            new_filename = filename_without_extension + '.png'

            # 构建源文件的完整路径
            source_path = os.path.join(source_dir, filename)

            # 构建目标文件的完整路径
            destination_path = os.path.join(destination_dir, new_filename)

            # 复制文件到目标路径
            shutil.copy2(source_path, destination_path)

            # 删除原始文件
            os.remove(source_path)