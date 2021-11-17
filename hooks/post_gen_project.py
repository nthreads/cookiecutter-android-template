import sys
import subprocess
import shutil

import os

#===== Variables =====
print("==== Post Gen Hook ====")

repo_name = '{{ cookiecutter.repo_name }}'
global_path = os.getcwd()

print("Name : ", repo_name)
print("Path : ", global_path) 

create_app_base         = '{{cookiecutter.create_app_base}}' == 'y'
create_fb_remote_config = '{{cookiecutter.create_fb_remote_config}}' == 'y'
create_location_utils   = '{{cookiecutter.create_location_utils}}' == 'y'

#===== Functions =====
def remove(filepath):
    if os.path.isfile(filepath):
        os.remove(filepath)
    elif os.path.isdir(filepath):
        shutil.rmtree(filepath)

def initial_commit():
    subprocess.call(['git', 'init'])
    subprocess.call(['git', 'add', '*'])
    subprocess.call(['git', 'commit', '-m', 'Initial commit from template project'])

def open_android_studio() :
    print('\n\n%s Opening in Android Studio...' % repo_name)

    subprocess.run(["open", "-a", "/Applications/Android Studio.app", global_path])

def remove_not_files_directories() :
    ab_main_path = 'app-base/src/main'
    ab_pkg_path = 'java/com/csms/base'

    if not create_app_base:
        print("---> Removing app base...")
        remove(os.path.join(global_path, 'app-base'))

    # No need to check if app-base directory is not created
    if not create_fb_remote_config and create_app_base:
        print("---> Removing firebase remote config kt file and default.xml...")
        
        remove(os.path.join(global_path, ab_main_path, ab_pkg_path, 'domain/models/RemoteConfig.kt'))
        remove(os.path.join(global_path, ab_main_path, 'res/xml/remote_config_defualts.xml'))
    
    # No need to check if app-base directory is not created
    if not create_fb_remote_config and create_app_base:
        print("---> Removing Location Utils...")
        
        remove(os.path.join(global_path, ab_main_path, ab_pkg_path, 'utils/LocationUtils.kt'))
    

#===== Execute Main =====

if os.path.exists(global_path) & str(global_path).endswith(str(repo_name)):

    remove_not_files_directories()

    open_android_studio()    
    
    # initial_commit()

    # exits with status 1 to indicate failure
    sys.exit(0)

else:
    print("Project folder doesnt exists")


