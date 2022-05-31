import sys
import numpy as np
import pandas as pd
from apyori import apriori

path_to_file = sys.argv[1]
support = float(sys.argv[2])
confidence = float(sys.argv[3])

store_data = pd.read_csv(path_to_file)

store_data = pd.read_csv(path_to_file, header=None)

records = []
for i in range(0, store_data.shape[0]):
    records.append([str(store_data.values[i,j]) for j in range(0, store_data.shape[1])])

association_rules = apriori(records, min_support= support, min_confidence= confidence, min_lift=3, min_length=2)
association_results = list(association_rules)

rules = []
for item in association_results:

    # first index of the inner list
    # Contains base item and add item
    pair = item[0] 
    items = [x for x in pair]
    if items[0] == 'nan' or items[1] == 'nan':
      continue
    
    rule = items[0] + " -> " + items[1]
    if rule in rules:
    	continue
    
    rules.append(rule)	
    print("Rule: " + items[0] + " -> " + items[1])

    #second index of the inner list
    print("Support: " + str(item[1]))

    #third index of the list located at 0th
    #of the third index of the inner list

    print("Confidence: " + str(item[2][0][2]))
    print("Lift: " + str(item[2][0][3]))
    print("=====================================")
