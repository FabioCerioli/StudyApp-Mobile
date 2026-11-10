import numpy as np


class FeatureExtractor:


    @staticmethod
    def extract(samples):


        if len(samples) != 128:
            raise ValueError("La finestra deve contenere 128 campioni")


        signals = {

            "ax": [],
            "ay": [],
            "az": [],

            "gx": [],
            "gy": [],
            "gz": []

        }


        for sample in samples:


            for axis in signals.keys():

                signals[axis].append(sample.get(axis,0))



        features = {}



        for name,values in signals.items():

            values = np.array(values)
            features[f"{name}_mean"] = float(np.mean(values))
            features[f"{name}_std"] = float(np.std(values))
            features[f"{name}_min"] = float(np.min(values))
            features[f"{name}_max"] = float(np.max(values))
            features[f"{name}_range"] = float(np.max(values)-np.min(values))

        return features