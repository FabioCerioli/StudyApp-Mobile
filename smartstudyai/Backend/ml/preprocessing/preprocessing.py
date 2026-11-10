import pandas as pd
import numpy as np
import os
import pickle


DATASET_PATH = "../../dataset/UCI HAR Dataset"



def load_signals(folder):

    path = os.path.join(
        DATASET_PATH,
        folder,
        "Inertial Signals"
    )


    signals = {}


    files = {

        "ax": f"body_acc_x_{folder}.txt",
        "ay":f"body_acc_y_{folder}.txt",
        "az": f"body_acc_z_{folder}.txt",


        "gx":f"body_gyro_x_{folder}.txt",
        "gy":f"body_gyro_y_{folder}.txt",
        "gz":f"body_gyro_z_{folder}.txt"

    }



    for name,file in files.items():

        signals[name] = pd.read_csv(

            os.path.join(path,file),

            sep="\s+",
            header=None

        ).values



    return signals




def extract_features(signals):


    samples = []


    n = signals["ax"].shape[0]


    for i in range(n):


        feature = {}


        for axis in [
            "ax",
            "ay",
            "az",
            "gx",
            "gy",
            "gz"
        ]:


            values = signals[axis][i]


            feature[f"{axis}_mean"] = (np.mean(values))


            feature[f"{axis}_std"] = (np.std(values))


            feature[f"{axis}_min"] = (np.min(values))


            feature[f"{axis}_max"] = (np.max(values))


            feature[f"{axis}_range"] = (np.max(values) - np.min(values))

        samples.append(feature)

    return pd.DataFrame(samples)




def convert_labels(y):

    return y[0].apply(

        lambda x:
        1 if x in [4,5]
        else 0

    )





for folder in ["train","test"]:


    print("Elaborazione:",folder)
    signals = load_signals(folder)

    X = extract_features(signals)

    y = pd.read_csv(

        os.path.join(
            DATASET_PATH,
            folder,
            f"y_{folder}.txt"
        ),

        header=None

    )


    y = convert_labels(y)



    print(X.shape,y.shape)


    pickle.dump(

        (X,y),

        open( f"../../dataset/{folder}_processed.pkl", "wb" )
    )



print("Preprocessing completato")