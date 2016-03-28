package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;

public class ArrayDifference {
	public static void main(String[] args) {
		Integer[] r3Array=new Integer[]{1, 3, 15, 19, 58, 1143, 87, 94, 122, 129, 133, 163, 166, 207, 213, 215, 205, 249, 281, 293, 283, 307, 316, 338, 347, 352, 355, 357, 365, 368, 369, 383, 389, 449, 569, 574, 579, 583, 608, 360, 617, 536, 68, 35, 82, 638, 641, 643, 646, 649, 650, 653, 654, 657, 666, 132, 679, 613, 725, 549, 743, 772, 776, 778, 786, 803, 586, 821, 831, 227, 838, 622, 710, 388, 871, 692, 907, 913, 915, 919, 921, 924, 925, 928, 203, 932, 935, 938, 939, 942, 522, 949, 441, 959, 962, 965, 968, 971, 447, 350, 973, 979, 378, 987, 990, 632, 845, 998, 997, 1003, 1005, 1010, 1021, 883, 861, 873, 412, 179, 1034, 45, 72, 1038, 687, 459, 507, 504, 508, 1054, 1058, 1061, 893, 469, 1070, 597, 1073, 1076, 22, 695, 186, 1084, 1093, 1094, 1097, 1104, 1107, 1108, 539, 1112, 1089, 1121, 268, 269, 1127, 1134, 1137, 1128, 1141, 85, 1147, 1151, 767, 1158, 1160, 1162, 1164, 1167, 1170, 1173, 26, 29, 30, 33, 41, 64, 67, 77, 80, 97, 571, 158, 162, 170, 183, 199, 202, 159, 219, 224, 242, 245, 252, 256, 258, 261, 272, 275, 286, 287, 276, 304, 311, 324, 327, 345, 363, 381, 386, 392, 395, 318, 435, 438, 446, 472, 481, 484, 235, 518, 521, 525, 528, 533, 556, 562, 565, 566, 582, 589, 593, 12, 604, 607, 216, 478, 625, 635, 660, 663, 674, 76, 106, 704, 707, 809, 190, 721, 724, 427, 734, 716, 503, 740, 751, 267, 744, 762, 765, 770, 789, 948, 794, 797, 806, 223, 814, 817, 824, 827, 599, 374, 836, 992, 849, 852, 857, 728, 253, 868, 877, 5, 892, 780, 628, 903, 911, 946, 953, 958, 341, 982, 984, 402, 727, 411, 413, 1017, 432, 1025, 1041, 1046, 1050, 1055, 895, 1065, 108, 143, 701, 1087, 1100, 1103, 1117, 1118, 1131, 535, 195, 23, 38, 50, 55, 74, 91, 125, 147, 180, 196, 220, 231, 239, 300, 303, 313, 322, 342, 372, 100, 328, 408, 426, 428, 431, 335, 401, 463, 476, 7, 490, 109, 498, 514, 517, 544, 558, 591, 602, 603, 458, 697, 713, 228, 756, 759, 783, 167, 319, 46, 105, 111, 144, 232, 104, 114, 116, 119, 140, 153, 575, 331, 423, 486, 266, 683, 420, 154, 175, 416, 419, 881, 686, 44, 69, 172, 511};
		Integer[] r4Array=new Integer[]{1, 3, 15, 19, 58, 1143, 87, 94, 122, 129, 133, 163, 166, 207, 213, 215, 205, 249, 281, 293, 283, 307, 316, 338, 347, 352, 355, 357, 365, 368, 369, 383, 389, 449, 569, 574, 579, 583, 608, 360, 617, 536, 68, 35, 82, 638, 641, 643, 646, 649, 650, 653, 654, 657, 666, 132, 679, 613, 725, 549, 743, 772, 776, 778, 786, 803, 586, 821, 831, 227, 838, 622, 710, 388, 871, 692, 907, 913, 915, 919, 921, 924, 925, 928, 203, 932, 935, 938, 939, 942, 522, 949, 441, 959, 962, 965, 968, 971, 447, 350, 973, 979, 378, 987, 990, 632, 845, 998, 997, 1003, 1005, 1010, 1021, 883, 861, 873, 412, 179, 1034, 45, 72, 1038, 687, 459, 507, 504, 508, 1054, 1058, 1061, 893, 469, 1070, 597, 1073, 1076, 22, 695, 186, 1084, 1093, 1094, 1097, 1104, 1107, 1108, 539, 1112, 1089, 1121, 268, 269, 1127, 1134, 1137, 1128, 1141, 85, 1147, 1151, 767, 1158, 1160, 1162, 1164, 1167, 1170, 1173, 6, 12, 25, 28, 31, 38, 41, 42, 52, 55, 64, 67, 69, 74, 78, 89, 92, 97, 100, 104, 109, 111, 114, 116, 119, 125, 571, 140, 144, 147, 153, 575, 158, 159, 162, 170, 172, 175, 180, 183, 196, 199, 202, 216, 219, 222, 224, 231, 239, 244, 252, 253, 256, 258, 261, 264, 272, 275, 286, 289, 298, 301, 304, 311, 315, 320, 323, 326, 328, 331, 342, 345, 363, 276, 381, 386, 392, 395, 318, 408, 418, 425, 428, 431, 437, 335, 446, 452, 105, 463, 472, 475, 7, 481, 484, 488, 490, 143, 142, 500, 235, 511, 557, 517, 520, 525, 528, 533, 535, 544, 266, 556, 558, 562, 565, 566, 582, 589, 591, 595, 599, 602, 605, 478, 625, 634, 637, 660, 663, 486, 676, 682, 685, 136, 420, 694, 698, 703, 706, 809, 712, 190, 721, 724, 228, 733, 736, 503, 738, 745, 751, 754, 280, 757, 761, 764, 770, 780, 783, 789, 948, 793, 375, 799, 806, 223, 814, 817, 824, 827, 829, 836, 992, 849, 852, 856, 728, 319, 868, 877, 5, 415, 44, 46, 892, 900, 902, 561, 946, 953, 958, 341, 982, 984, 402, 727, 1014, 1017, 432, 1025, 686, 232, 106, 1039, 1046, 1050, 1055, 895, 467, 108, 701, 1087, 1100, 1103, 537, 1117, 1118, 1131, 154, 195};
	  Integer[] r3r4Array=new Integer[]{1, 3, 15, 19, 58, 1143, 87, 94, 122, 129, 133, 163, 166, 207, 213, 215, 205, 249, 281, 293, 283, 307, 316, 338, 347, 352, 355, 357, 365, 368, 369, 383, 389, 449, 569, 574, 579, 583, 608, 360, 617, 536, 68, 35, 82, 638, 641, 643, 646, 649, 650, 653, 654, 657, 666, 132, 679, 613, 725, 549, 743, 772, 776, 778, 786, 803, 586, 821, 831, 227, 838, 622, 710, 388, 871, 692, 907, 913, 915, 919, 921, 924, 925, 928, 203, 932, 935, 938, 939, 942, 522, 949, 441, 959, 962, 965, 968, 971, 447, 350, 973, 979, 378, 987, 990, 632, 845, 998, 997, 1003, 1005, 1010, 1021, 883, 861, 873, 412, 179, 1034, 45, 72, 1038, 687, 459, 507, 504, 508, 1054, 1058, 1061, 893, 469, 1070, 597, 1073, 1076, 22, 695, 186, 1084, 1093, 1094, 1097, 1104, 1107, 1108, 539, 1112, 1089, 1121, 268, 269, 1127, 1134, 1137, 1128, 1141, 85, 1147, 1151, 767, 1158, 1160, 1162, 1164, 1167, 1170, 1173, 26, 29, 30, 33, 41, 64, 67, 77, 80, 97, 571, 158, 162, 170, 183, 199, 202, 159, 219, 224, 242, 245, 252, 256, 258, 261, 272, 275, 286, 287, 276, 304, 311, 324, 327, 345, 363, 381, 386, 392, 395, 318, 435, 438, 446, 472, 481, 484, 235, 518, 521, 525, 528, 533, 556, 562, 565, 566, 582, 589, 593, 12, 604, 607, 216, 478, 625, 635, 660, 663, 674, 76, 106, 704, 707, 809, 190, 721, 724, 427, 734, 716, 503, 740, 751, 267, 744, 762, 765, 770, 789, 948, 794, 797, 806, 223, 814, 817, 824, 827, 599, 374, 836, 992, 849, 852, 857, 728, 253, 868, 877, 5, 892, 780, 628, 903, 911, 946, 953, 958, 341, 982, 984, 402, 727, 411, 413, 1017, 432, 1025, 1041, 1046, 1050, 1055, 895, 1065, 108, 143, 701, 1087, 1100, 1103, 1117, 1118, 1131, 535, 195, 23, 38, 50, 55, 74, 91, 125, 147, 180, 196, 220, 231, 239, 300, 303, 313, 322, 342, 372, 100, 328, 408, 426, 428, 431, 335, 401, 463, 476, 7, 490, 109, 498, 514, 517, 544, 558, 591, 602, 603, 458, 697, 713, 228, 756, 759, 783, 167, 319, 46, 105, 111, 144, 232, 104, 114, 116, 119, 140, 153, 575, 331, 423, 486, 266, 683, 420, 154, 175, 416, 419, 881, 686, 44, 69, 172, 511};
		
		List<Integer> r3List=Arrays.asList(r3Array);
		List<Integer> r4List=Arrays.asList(r4Array);
		System.out.println(r3List.size());
		System.out.println(r4List.size());
		List<Integer> r3r4List=Arrays.asList(r3r4Array);
		
		 
		
		Collection<Integer> r3r4Diff=CollectionUtils.subtract(r3List, r4List);
		int r3r4DiffSize=r3r4Diff.size();
		System.out.println(r3r4DiffSize);
		System.out.println(r3r4Diff);
		
		Collection<Integer> r4r3Diff=CollectionUtils.subtract(r4List, r3List);
		System.out.println(r4r3Diff.size());
		System.out.println(r4r3Diff);
	}
}