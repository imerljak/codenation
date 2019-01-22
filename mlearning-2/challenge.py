import pandas as pd
import numpy as np

training_df = pd.read_csv("train.csv")
training_df['DT_INGRESSO_CURSO'] = pd.to_datetime(
    training_df['DT_INGRESSO_CURSO']).astype(int)

training_x_df = training_df[training_df.columns.difference(
    ['index', 'NO_OCDE_AREA_GERAL'])]

training_y_df = training_df['NO_OCDE_AREA_GERAL']

training_x_df.to_csv('training_x.csv', index=False)
training_y_df.to_csv('training_y.csv', index=False)


training_percentage = 0.9

x_values = training_x_df.values
y_values = training_y_df.values

x_dummies_df = x_values[:int(len(training_x_df) * training_percentage)]
y_dummies_df = y_values[:int(len(training_x_df) * training_percentage)]

x_predict = x_values[-(len(training_x_df) - len(x_dummies_df)):]
y_predict = y_values[-(len(training_y_df) - len(y_dummies_df)):]

x_predict = np.nan_to_num(x_predict)
y_predict = np.nan_to_num(y_predict)


treino_x = x_dummies_df
treino_y = y_dummies_df

treino_x = np.nan_to_num(treino_x)
treino_y = np.nan_to_num(treino_y)

from sklearn.neighbors import KNeighborsClassifier

modelo2 = KNeighborsClassifier()

modelo2.fit(treino_x, treino_y)

test_df = pd.read_csv('test.csv')
test_df['DT_INGRESSO_CURSO'] = pd.to_datetime(
    test_df['DT_INGRESSO_CURSO']).astype(int)

test_dummies_df = test_df[test_df.columns.difference(
    ['index', 'NO_OCDE_AREA_GERAL'])]

test_x = test_dummies_df.values

test_x = np.nan_to_num(test_x)

# print(test_dummies_df.describe())
# print(x_dummies_df.describe())

res2 = modelo2.predict(x_predict)

print(modelo2.score(x_predict, y_predict))

index = test_df['index']

pd.concat([index, pd.DataFrame(res2, columns=['NO_OCDE_AREA_GERAL'])],
          axis=1).to_csv('answer.csv', index=False)
