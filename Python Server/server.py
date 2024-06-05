from PIL import Image, ImageEnhance, ImageFilter
import pytesseract
import base64
from io import BytesIO

# Setează calea către tesseract executabil
pytesseract.pytesseract.tesseract_cmd = 'C:/Program Files/Tesseract-OCR/tesseract.exe'

def preprocess_image(image_data):
    # Convert byte array la imagine PIL
    image = Image.open(BytesIO(image_data))

    gray_image = image.convert('L')

    enhancer = ImageEnhance.Sharpness(gray_image)
    enhanced_image = enhancer.enhance(2.0)

    contrast_enhancer = ImageEnhance.Contrast(enhanced_image)
    contrast_image = contrast_enhancer.enhance(2.0)

    blurred_image = contrast_image.filter(ImageFilter.MedianFilter())

    return blurred_image

def extract_text_from_image(image_data):
    preprocessed_image = preprocess_image(image_data)
    text = pytesseract.image_to_string(preprocessed_image, config='--psm 6')
    return text

def verify_student_card(image_data):
    extracted_text = extract_text_from_image(image_data)
    extracted_text=extracted_text.lower()
    print(extracted_text)
    facultatea_found = "facultatea" in extracted_text
    univeristatea_found = "universitatea" in extracted_text
    scoala_found = "unitate de învățământ" in extracted_text

    if facultatea_found or univeristatea_found:
        return "Student"
    elif scoala_found:
        return "Elev"
    else:
        return "Invalid"

# Flask app
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/process_image', methods=['POST'])
def process_image():
    try:
        # Primesc imaginea sub forma de byte[]
        #image_bytes = request.data
        image_bytes = request.files['file'].read()

        # Verific dacă imaginea este nenulă
        if image_bytes:
            result = verify_student_card(image_bytes)
            return jsonify({'result': result})
        else:
            return jsonify({'error': 'Nu s-a primit nicio imagine.'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
