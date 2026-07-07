import cv2

sr = cv2.dnn_superres.DnnSuperResImpl_create()
sr.readModel("EDSR_x4.pb")
sr.setModel("edsr", 4)


image = cv2.imread("imageofman.jpeg")

result = sr.upsample(image)

cv2.namedWindow("Enhanced", cv2.WINDOW_NORMAL)
cv2.resizeWindow("Enhanced", 600, 900)

cv2.namedWindow("normal", cv2.WINDOW_NORMAL)
cv2.resizeWindow("normal", 600, 900)





cv2.imshow("Enhanced", result)
cv2.imshow("normal", image)
cv2.waitKey()