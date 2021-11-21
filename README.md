# Android Boilerplate template using Cookiecutter

A [cookiecutter](https://github.com/cookiecutter/cookiecutter) :cookie: template for bootstrapping new Android projects

## Usage

#### Step 1

Install cookiecutter (via homebrew on mac/linux):

```bash
brew install cookiecutter
```

#### Step 2

Assuming you have `git` installed, there are two ways you can setup/configure the Android App (Choose one):

1. Directly via `cookiecutter`. 

    Simply execute the below one-liner command in your terminal.
    ```
    cookiecutter gh:nthreads/cookiecutter-android-template
    ````
1. Via cloning this repository

    - using `https`

        ```bash
        git clone https://github.com/nthreads/cookiecutter-android-template
        ```

    - using `ssh`
        ```bash
        git clone git@github.com:nthreads/cookiecutter-android-template
        ```

    When done, run `cookiecutter` tool by passing in the template directory as an argument:

    ```bash
    cookiecutter cookiecutter-android-template/
    ```
