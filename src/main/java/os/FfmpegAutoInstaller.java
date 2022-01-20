package os;

import os.win.powershell.Powershell;
import tools.audio_file_converter.FfmpegCliAccess;

import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class FfmpegAutoInstaller {

    public static boolean askForAutomaticInstallation() {
        System.out.println("\nAn installation of ffmpeg is required to use this command.");
        System.out.println("Would you like to install ffmpeg automatically? (y/n)");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        return scanner.hasNext("y");
    }

    public static int install() {
        int exitValue = 0;
        final OperatingSystemChecker.OSType osType = OperatingSystemChecker.getOperatingSystemType();
        if(osType == OperatingSystemChecker.OSType.WINDOWS) {
            // check if chocolatey is installed, install it if not
            if(Powershell.call("chocolatey", "version") != 0) {
                // TODO: install chocolatey!
                System.out.println("Chocolatey is not installed on your machine.");
                exitValue = Powershell.callAndLog("Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))");
            }
            if(exitValue != 0) {
                return exitValue;
            }
            // install tha ffmpeg yus
            return Powershell.callAndLog("y".getBytes(), "choco", "install ffmpeg -y");
        }
        else if(osType == OperatingSystemChecker.OSType.LINUX) {
            System.out.println("Automatic installation of ffmpeg on linux isn't implemented yet!");
            System.out.println("Exiting...");
            System.exit(-1);
        }
        else if(osType == OperatingSystemChecker.OSType.MAC_OS) {
            System.out.println("Automatic installation of ffmpeg on mac-os isn't implemented yet!");
            System.out.println("Exiting...");
            System.exit(-1);
        }
        return -1;
    }

    public static int applyInstallationProcedure() throws InterruptedException {
        int exitValue = 0;

        if(FfmpegCliAccess.notInstalled()) {
            if(FfmpegAutoInstaller.askForAutomaticInstallation()) {
                System.out.println("Some additional software might be downloaded to help with the installation process.");
                System.out.println("If there are errors during the installation, try running the app with administrator privileges.\n");
                System.out.println("Installing ffmpeg...");
                TimeUnit.SECONDS.sleep(4);
                exitValue = FfmpegAutoInstaller.install();
                System.out.println("\n================================================================\n");
                if(exitValue == 0) {
                    System.out.println("ffmpeg was installed successfully!\n\n");
                }
                else {
                    System.out.println("Installation of ffmpeg failed!\n\n");
                }
                TimeUnit.SECONDS.sleep(2);
            }
            else {
                System.out.println("\n\nerror: ffmpeg needs to be installed and accessible via the command line to use this command.");
                exitValue = -1;
            }
        }

        return exitValue;
    }

    public static void applyStrictInstallationProcedure() throws InterruptedException {
        int exitValue = FfmpegAutoInstaller.applyInstallationProcedure();
        if(exitValue != 0) {
            System.out.println("exiting...");
            System.exit(exitValue);
        }
    }
}
