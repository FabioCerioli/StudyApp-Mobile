import pickle
import pandas as pd
import numpy as np

from ml.preprocessing.feature_extractor import FeatureExtractor


MODEL_PATH = "ml/models/random_forest.pkl"


model = pickle.load(
    open(MODEL_PATH,"rb")
)


# CONFIGURAZIONE

MOVEMENT_THRESHOLD = 0.15


# CALCOLO DEL MOVIMENTO

def calculate_movement(samples):

    accelerometer = []
    gyroscope = []

    for sample in samples:

        ax = float(sample.get("ax", 0))
        ay = float(sample.get("ay", 0))
        az = float(sample.get("az", 0))

        gx = float(sample.get("gx", 0))
        gy = float(sample.get("gy", 0))
        gz = float(sample.get("gz", 0))

        # Intensità accelerometro
        acceleration = np.sqrt(ax**2 +ay**2 +az**2)

        # Intensità giroscopio
        rotation = np.sqrt(gx**2 +gy**2 +gz**2)

        accelerometer.append(acceleration)
        gyroscope.append(rotation)

    accelerometer = np.array(accelerometer)
    gyroscope = np.array(gyroscope)

    # Variabilità del movimento
    acceleration_movement = np.std(accelerometer)

    rotation_movement = np.std(gyroscope)

    # Movimento complessivo
    movement = (acceleration_movement + rotation_movement) 

    return float(movement)




# PREDIZIONE

def predict(samples):


    # 1. ESTRAZIONE FEATURE

    features = FeatureExtractor.extract(samples)


    X = pd.DataFrame([features])


    # 2. PREDIZIONE MODELLO

    prediction = model.predict(X)[0]


    probability = model.predict_proba(X)[0]


    confidence = max(probability)


    # 3. CALCOLO MOVIMENTO

    movement = calculate_movement(samples)


    # 4. PREDIZIONE ORIGINALE

    original_prediction = (
        "study"
        if prediction == 1
        else
        "non_study"
    )


    # 5. POST-PROCESSING

    final_prediction = original_prediction


    if movement < MOVEMENT_THRESHOLD:
        final_prediction = "study"
    else:
        final_prediction = original_prediction
        

    # 6. RISULTATO
    print(
    "Movement:", movement,
    "ML:",original_prediction,
    "Final:",final_prediction
    )

    return {
        "prediction":final_prediction,
        "confidence":float(confidence),
        "features":len(features),
    }