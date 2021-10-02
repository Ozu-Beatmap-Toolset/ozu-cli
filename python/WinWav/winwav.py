import os
import subprocess
import sys
from pydub import AudioSegment

HELP_COMMAND_DISPLAY_INFORMATION = "WinWav v0.0.1\n\nList of commands:\n\n" \
                                   "python winwav.py -help\n" \
                                   "python winwav.py [audio-file-location] [folder-destination]\n"

CHOCOLATEY_HELP_COMMAND = ["choco", "-?"]
CHOCOLATEY_INSTALL_COMMAND = ["Set-ExecutionPolicy Bypass -Scope Process -Force; ["
                              "System.Net.ServicePointManager]::SecurityProtocol = ["
                              "System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object "
                              "System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1')) "]

FFMPEG_HELP_COMMAND = ["ffmpeg", "-?"]
CHOCO_INSTALL_FFMPEG_COMMAND = ["choco", " install", "ffmpeg"]


def main(args):
    if args[0] == "-h" or args[0] == "-help" or args[0] == "--help" or args[0] == "-?":
        print(HELP_COMMAND_DISPLAY_INFORMATION)
        exit(0)
    setup_chocolatey()
    setup_ffmpeg()
    export(args)


def setup_chocolatey():
    if run_silent_stdout(lambda: subprocess.call(CHOCOLATEY_HELP_COMMAND)) != 0:
        subprocess.call(CHOCOLATEY_INSTALL_COMMAND)


def setup_ffmpeg():
    if run_silent_stdout(lambda: subprocess.call(FFMPEG_HELP_COMMAND)) != 0:
        subprocess.call(CHOCO_INSTALL_FFMPEG_COMMAND)


def export(args):
    sound = AudioSegment.from_mp3(args[0])
    sound.export(args[1], format="wav")
    print("Conversion done successfully!")


def run_silent_stdout(func):

    old_stdout = sys.stdout
    sys.stdout = open(os.devnull, "w")
    a = func.__call__()
    sys.stdout = old_stdout
    return a


if __name__ == '__main__':
    main(sys.argv[1:])
