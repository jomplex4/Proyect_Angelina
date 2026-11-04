import sys
from figs import FIGS, HEIGHT
import figs_2023, figs_2024, figs_2025, figs_s1geo, figs_s2geo, figs_s3geo, figs_s4geo, figs_s1est, figs_s4est
from figgen import sheet
pre = sys.argv[2] if len(sys.argv) > 2 else ""
sheet({k: v for k, v in FIGS.items() if k.startswith(pre)}, sys.argv[1], heights=HEIGHT)
