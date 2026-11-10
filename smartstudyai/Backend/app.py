from flask import Flask
from flask import request
from flask import jsonify

from ml.predictor import predict


app = Flask(__name__)


@app.route("/")
def home():

    return jsonify({"status": "running"})


@app.route("/predict",methods=["POST"])
def predict_endpoint():

    data = request.get_json()
    result = predict(data["samples"])

    return jsonify(result)


if __name__ == "__main__":

    app.run(
        host="0.0.0.0",
        port=8000,
        debug=True
    )