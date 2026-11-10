import pickle

from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report
from sklearn.metrics import accuracy_score



X_train,y_train = pickle.load(
    open("../../dataset/train_processed.pkl", "rb")
)



X_test,y_test = pickle.load(
    open("../../dataset/test_processed.pkl","rb")
)



model = RandomForestClassifier(
    n_estimators=200,
    random_state=1,
    class_weight="balanced"

)



print("Training...")


model.fit(X_train,y_train)



pred = model.predict(X_test)



print("Accuracy:",accuracy_score(y_test,pred))


print(classification_report(y_test,pred))



pickle.dump(
    model,
    open("../models/random_forest.pkl","wb")

)

print("Modello salvato")